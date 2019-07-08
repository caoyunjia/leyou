package com.leyou.test.respostory;

import com.leyou.test.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item,Long> {

    List<Item> findItemsByPriceBetween(Double begin, Double end);
}
