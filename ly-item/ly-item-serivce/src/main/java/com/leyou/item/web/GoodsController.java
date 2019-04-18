package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.item.service.SpuService;
import com.leyou.item.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {
    @Autowired
    private SpuService spuService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable) {
        PageResult<Spu> pageResult = spuService.querySpusByPage(page, rows, sortBy, desc, key, saleable);
        return ResponseEntity.ok(pageResult);

    }

    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(GoodsVO goodsVO){

        spuService.saveGoods(goodsVO);
        return ResponseEntity.ok().build();
    }

}
