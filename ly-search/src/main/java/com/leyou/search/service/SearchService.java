package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class SearchService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private ElasticsearchTemplate template;

    public Goods buildGoods(Spu spu) {
        //查询分类
        List<Category> categories = categoryClient.queryCategoryListByPid(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        List<String> categoryNames = categories.stream().map(Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        //搜索字段
        String all = spu.getTitle() + StringUtils.join(categoryNames, " ") + brand.getName();
        //查询sku
        List<Sku> skuList = goodsClient.querySkuBySpuId(spu.getId());

        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnums.GOODS_SKU_NOT_FOUND);
        }
        ArrayList<Map<String, Object>> skusMapList = new ArrayList<>();
        List<Long> priceList = new ArrayList<>();
        skuList.forEach(sku -> {
            HashMap<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("price", sku.getPrice());
            skuMap.put("title", sku.getTitle());
            skuMap.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            skusMapList.add(skuMap);
            priceList.add(sku.getPrice());
        });
        List<SpecParam> params = specificationClient.queryParamsByCid(spu.getCid3());
        if (CollectionUtils.isEmpty(params)) {
            throw new LyException(ExceptionEnums.SPEC_PARAM_NOT_FOUND);
        }

        //查询详情
        SpuDetail spuDetail = goodsClient.queryDetailById(spu.getId());
        //获取所有的通用参数
        Map<String, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), String.class, String.class);
        Map<String, List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<String>>>() {
        });
        HashMap<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            String key = param.getName();
            Object value = "";
            //如果是通用属性,从通用属性里取，否则从其他属性里取
            if (param.getGeneric()) {
                if (param.getNumeric()) {
                    //如果时数值
                    value = chooseSegment(value.toString(), param);
                } else {
                    value = genericSpec.get(key);
                }
            } else {
                value = specialSpec.get(key);
            }
            specs.put(key, value);
        }
        //构建Goods对象
        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setCreateTime(spu.getCreateTime());
        goods.setAll(all);
        goods.setPrice(priceList);
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specs);
        return goods;

    }


    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }





    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            return null;
        }
        int page = request.getPage() - 1;
        int size = request.getDefaultRows();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, new String[]{});
        queryBuilder.withSourceFilter(fetchSourceFilter);
        queryBuilder.withPageable(PageRequest.of(page, size));
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", key));
        //此处获得分类参数
        String categoryAggName = "category_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        Aggregations aggs = result.getAggregations();
        List<Category> categories = parseCategoryAgg(aggs.get(categoryAggName));

        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));

        long total = result.getTotalElements();
        List<Goods> goods = result.getContent();

        int totalPages = result.getTotalPages();
        return new SearchResult(total, totalPages, goods, categories, brands);
    }

    private List<Brand> parseBrandAgg(LongTerms terms) {
        try {
            List<Long> brandIds = terms.getBuckets()
                    .stream().map(a -> a.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Brand> brands = brandClient.queryBrandByIds(brandIds);
            return brands;
        } catch (Exception e) {
            log.error("搜索服务查询品牌异常:",e);
            return null;
        }
    }

    private List<Category> parseCategoryAgg(LongTerms terms) {
        try {
            List<Long> categoryIds = terms.getBuckets().
                    stream().map(a -> a.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            return categoryClient.queryCategoryByIds(categoryIds);
        } catch (Exception e) {
            log.error("搜索服务查询分类异常:",e);
            return null;
        }
    }
}
