package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import com.leyou.item.vo.GoodsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<Spu> querySpusByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key, Boolean saleable) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(sortBy)){
            String orderByClause=sortBy+(desc?" DESC":" ASC");
            example.setOrderByClause(orderByClause);
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title","%"+key+"%");
        }

        List<Spu> spus = spuMapper.selectByExample(example);
        loadCategoryAndBrandName(spus);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);
        //如果集合为空
        if(CollectionUtils.isEmpty(spus)){
            throw new LyException(ExceptionEnums.GOODS_NOT_FOUND);
        }

        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    private void loadCategoryAndBrandName(List<Spu> spus){
        for (Spu spu : spus) {
            List<String> names = categoryService.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())).stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names, "/"));
            spu.setBname(brandService.queryBrandById(spu.getBrandId()).getName());
        }

    }
    @Transactional
    public void saveGoods(GoodsVO goodsVO) {
        //保存spu
        goodsVO.setId(null);
        goodsVO.setCreateTime(new Date());
        goodsVO.setLastUpdateTime(goodsVO.getCreateTime());
        goodsVO.setValid(false);
        goodsVO.setSaleable(true);

        int count = spuMapper.insert(goodsVO);
        if (count!=1) {
                throw new LyException(ExceptionEnums.GOODS_SAVE_ERROR);
        }
        //保存详情
        Long spuId = goodsVO.getId();
        SpuDetail spuDetail = goodsVO.getSpuDetail();
        spuDetail.setSpuId(spuId);
        spuDetailMapper.insert(spuDetail);

       saveSkuAndStock(goodsVO);
        //保存后生成模板
        amqpTemplate.convertAndSend("item.insert",goodsVO.getId());
    }


    /**
     * 保存sku和stock
     * @param goodsVO
     */
    private void saveSkuAndStock(GoodsVO goodsVO) {
        List<Sku> skus = goodsVO.getSkus();
        ArrayList<Stock> stocks = new ArrayList<>();
        for (Sku sku : skus) {
            //保存sku
            sku.setId(null);
            sku.setSpuId(goodsVO.getId());
            sku.setCreateTime(goodsVO.getCreateTime());
            sku.setLastUpdateTime(goodsVO.getCreateTime());
            int count= skuMapper.insert(sku);
            if (count!=1) {
                throw new LyException(ExceptionEnums.GOODS_SAVE_ERROR);
            }
            //批量新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);
        }
        int count = stockMapper.insertList(stocks);
        if(count!=stocks.size()){
            throw new LyException(ExceptionEnums.GOODS_SAVE_ERROR);
        }
    }


    public SpuDetail queryDetailById(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if (spuDetail == null) {
            throw new LyException(ExceptionEnums.GOODS_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }


    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skus)) {
            throw new LyException(ExceptionEnums.GOODS_SKU_NOT_FOUND);
        }
        List<Long> ids = skus.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stocks = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skus)) {
            throw new LyException(ExceptionEnums.GOODS_STOCK_NOT_FOUND);
        }
        Map<Long, Integer> stockMap = stocks.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skus.forEach(s -> s.setStock(stockMap.get(s.getId())));
        return skus;
    }

    /**
     * 修改goods
     * @param goodsVO
     */
    @Transactional
    public void updateGoods(GoodsVO goodsVO) {
        //删除stock
        Sku sku = new Sku();
        sku.setSpuId(goodsVO.getId());
        List<Sku> skuList = skuMapper.select(sku);
        if (!CollectionUtils.isEmpty(skuList)) {
            skuMapper.delete(sku);
            List<Long> skuIds = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(skuIds);
        }

        goodsVO.setValid(null);
        goodsVO.setSaleable(null);
        goodsVO.setCreateTime(null);
        goodsVO.setLastUpdateTime(new Date());
        int count = spuMapper.updateByPrimaryKeySelective(goodsVO);
        if(count!=1){
            throw new LyException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }
        count = spuDetailMapper.updateByPrimaryKeySelective(goodsVO.getSpuDetail());
        if(count!=1){
            throw new LyException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }
        //保存sku和stock
        Long spuId = goodsVO.getId();
        SpuDetail spuDetail = goodsVO.getSpuDetail();
        spuDetail.setSpuId(spuId);
        //修改detail
        spuDetailMapper.insert(spuDetail);
        //保存sku和stock
        saveSkuAndStock(goodsVO);

        //保存后生成模板
        amqpTemplate.convertAndSend("item.update",goodsVO.getId());
    }

    public Spu querySpuBySpuId(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new LyException(ExceptionEnums.GOODS_SPU_NOT_FOUND);
        }
        List<Sku> skus = querySkusBySpuId(id);
        spu.setSkus(skus);
        SpuDetail spuDetail = queryDetailById(id);
        spu.setSpuDetail(spuDetail);

        return spu;
    }
}
