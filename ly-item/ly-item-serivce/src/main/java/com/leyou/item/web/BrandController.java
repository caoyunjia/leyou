package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import com.leyou.item.vo.BrandVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiOperation(value = "查询品牌分页接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "每页显示条数", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "desc", value = "是否正序", paramType = "query", dataType = "Boolean"),
            @ApiImplicitParam(name = "key", value = "查询关键字", paramType = "query", dataType = "String"),
    })
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
    @ApiOperation(value = "根据分类查品牌列表")
    @ApiParam(name = "cid", value = "分类Id",type = "Long")
    public ResponseEntity<List<Brand>> queryBrandByPage(@PathVariable(name = "cid") Long cid){

        return ResponseEntity.ok(brandService.queryBrandsByCid(cid));
    }

    @PostMapping
    @ApiOperation(value = "保存品牌信息")
    public ResponseEntity<Void> save(BrandVo brandVo) {

        brandService.saveBrand(brandVo);

         return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{id}")
    @ApiOperation(value = "查询品牌明细")
    @ApiParam(name = "id", value = "品牌Id",type = "Long")
    public ResponseEntity<Brand> queryBrandById( @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(brandService.queryBrandById(id));
    }


    @GetMapping("brands")
    @ApiOperation(value = "根据多个品牌查询品牌列表")
    public ResponseEntity<List<Brand>> queryBrandsByIds(@RequestParam(name = "ids") List<Long> ids){

        return ResponseEntity.ok(brandService.queryBrandsByIds(ids));
    }
}
