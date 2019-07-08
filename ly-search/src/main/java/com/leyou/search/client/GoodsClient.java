package com.leyou.search.client;

import com.leyou.item.api.GoodApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("LY-ITEM")
public interface GoodsClient extends GoodApi{


}
