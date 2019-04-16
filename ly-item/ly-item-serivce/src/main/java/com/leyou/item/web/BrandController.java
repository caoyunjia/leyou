package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import com.leyou.item.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("brand")
@RestController
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key) {

        PageResult<Brand> result=brandService.queryBrandByPage(page, rows, sortBy, desc, key);

        return ResponseEntity.ok(result);
    }


    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByPage(@PathVariable(name = "cid") Long cid){

        return ResponseEntity.ok(brandService.queryBrandsByCid(cid));
    }

    @PostMapping
    public ResponseEntity<Void> save(BrandVo brandVo) {

        brandService.saveBrand(brandVo);

         return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(brandService.queryBrandById(id));
    }
}
