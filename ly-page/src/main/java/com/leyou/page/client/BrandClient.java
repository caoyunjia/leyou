package com.leyou.page.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("LY-ITEM")
public interface BrandClient extends BrandApi {
}
