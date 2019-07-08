package com.leyou.test;

import com.leyou.test.pojo.Item;
import com.leyou.test.respostory.ItemRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchTest {

    /**
     * 一般不用
     */


    @Autowired
    private ElasticsearchTemplate template;


    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void toCreateIndex() {
        //创建索引
        template.createIndex(Item.class);
        //映射索引,该步骤不能省略
        template.putMapping(Item.class);
    }


    @Test
    public void indexList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "小米手机7", "手机", "小米", 3699.00, "http://image.leyou.com/123.jpg"));
        list.add(new Item(2L, "坚果手机R1", " 手机", "锤子", 3299.00, "http://image.leyou.com/123.jpg"));
        list.add(new Item(3L, "荣耀V10", " 手机", "华为", 3699.00, "http://image.leyou.com/123.jpg"));
        list.add(new Item(4L, "小米Max2S", " 手机", "小米", 3899.00, "http://image.leyou.com/123.jpg"));
        list.add(new Item(5L, "华为META10", " 手机", "华为", 4499.00, "http://image.leyou.com/3.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }

    @Test
    public void testFindAll() {
        Iterable<Item> items = itemRepository.findAll();
        for (Item item : items) {
            System.out.println(item);
        }

    }


    @Test
    public void testFindBy() {
        List<Item> items = itemRepository.findItemsByPriceBetween(3699.0, 4499.0);
        for (Item item : items) {
            System.out.println(item);
        }
    }


    @Test
    public void testQuery() {
        Iterable<Item> items = itemRepository.search(QueryBuilders.matchQuery("title", "小米"));
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.println("---------");
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        //结果过滤
        searchQuery.withSourceFilter(new FetchSourceFilter(new String[]{"id", "brand", "price"}, null));
        //排序
        searchQuery.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        //分页
        searchQuery.withPageable(PageRequest.of(0, 2));
        Page<Item> itemPage = itemRepository.search(searchQuery.build());
        for (Item item : itemPage) {
            System.out.println(item);
        }
    }


    @Test
    public void testAgg() {
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        searchQuery.addAggregation(AggregationBuilders.terms("popular_brand").field("brand"));
        //查询并返回聚合结果
        AggregatedPage<Item> result = template.queryForPage(searchQuery.build(), Item.class);
        for (Item item : result) {
            System.out.println(item);
        }
        Map<String, Object> brand = result.getAggregations().get("popular_brand").getMetaData();
        StringTerms terms = result.getAggregations().get("popular_brand");
        List<StringTerms.Bucket> buckets = terms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            System.out.println(bucket.getKeyAsString());
            System.out.println(bucket.getDocCount());

        }

    }
}
