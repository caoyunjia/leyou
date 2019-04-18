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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpuService {
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
            throw new LyException(ExceptionEnums.SPU_NOT_FOUND);
        }

        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    private void loadCategoryAndBrandName(List<Spu> spus){
        for (Spu spu : spus) {
            List<String> names = categoryService.queryCategoryListByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())).stream().map(Category::getName).collect(Collectors.toList());
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

        List<Sku> skus = goodsVO.getSkus();
        ArrayList<Stock> stocks = new ArrayList<>();
        for (Sku sku : skus) {
            //保存sku
            sku.setId(null);
            sku.setSpuId(spuId);
            sku.setCreateTime(goodsVO.getCreateTime());
            sku.setLastUpdateTime(goodsVO.getCreateTime());
            count= skuMapper.insert(sku);
            if (count!=1) {
                throw new LyException(ExceptionEnums.GOODS_SAVE_ERROR);
            }
            //批量新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);
        }
        count = stockMapper.insertList(stocks);
        if(count!=stocks.size()){
            throw new LyException(ExceptionEnums.GOODS_SAVE_ERROR);
        }

    }
}
