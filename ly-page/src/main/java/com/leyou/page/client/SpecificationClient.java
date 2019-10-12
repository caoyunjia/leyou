package com.leyou.page.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("ly-item")
public interface SpecificationClient  extends SpecificationApi {
}
