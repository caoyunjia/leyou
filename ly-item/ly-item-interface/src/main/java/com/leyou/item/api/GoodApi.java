package com.leyou.item.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodApi {

    @GetMapping("goods/spu/detail/{id}")
    SpuDetail queryDetailById(@PathVariable(name = "id") Long id);

    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam(name = "id") Long spuId);
    @GetMapping("spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable);


    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable(name = "id") Long id);
}
