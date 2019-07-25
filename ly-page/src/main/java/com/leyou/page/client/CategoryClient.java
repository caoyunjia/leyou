package com.leyou.page.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("LY-ITEM")
public interface CategoryClient extends CategoryApi {

}
