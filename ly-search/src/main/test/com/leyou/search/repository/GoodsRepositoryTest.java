package com.leyou.search.repository;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsRepositoryTest {
    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;


    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate template;
    @Test
    public void testCreateIndex() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);

    }

    @Test
    public void testDeleteIndex() {
        template.deleteIndex(Goods.class);

    }

    @Test
    public void loadData(){
        int page=1;
        int row = 100;
        int size;
        do {
            PageResult<Spu> pageResult = goodsClient.querySpuByPage(page, row, null, null, null, null);
            List<Spu> items = pageResult.getItems();
            if (CollectionUtils.isEmpty(items)) {
                break;
            }
            List<Goods> goodsList = items.stream().map(searchService::buildGoods).collect(Collectors.toList());
            goodsRepository.saveAll(goodsList);
            size = goodsList.size();
            page++;
        }while (size==100);

    }
}