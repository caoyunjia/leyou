package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import com.leyou.item.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable) {
        PageResult<Spu> pageResult = goodsService.querySpusByPage(page, rows, sortBy, desc, key, saleable);
        return ResponseEntity.ok(pageResult);

    }

    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody GoodsVO goodsVO){

        goodsService.saveGoods(goodsVO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody GoodsVO goodsVO){

        goodsService.updateGoods(goodsVO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @GetMapping("goods/spu/detail/{id}")
    public ResponseEntity<SpuDetail> queryDetailById(@PathVariable(name = "id") Long spuId){
        return ResponseEntity.ok(goodsService.queryDetailById(spuId));
    }


    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam(value = "id") Long spuId){
        return ResponseEntity.ok(goodsService.querySkusBySpuId(spuId));
    }


    @GetMapping("sku/list/ids")
    public ResponseEntity<List<Sku>> querySkusByIds(@RequestParam(value = "ids") List<Long> ids){
        return ResponseEntity.ok(goodsService.querySkusByIds(ids));
    }


    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(goodsService.querySpuBySpuId(id));
    }

}

