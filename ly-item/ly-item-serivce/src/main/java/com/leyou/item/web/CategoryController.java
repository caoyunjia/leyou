package com.leyou.item.web;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import com.leyou.item.vo.BrandVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(Long pid){

        List<Category> categories=categoryService.queryCategoryListByPid(pid);

        return ResponseEntity.ok(categories);

    }

    /**
     * 根据Id集合查找分类
     * @param ids ids 必须加requestParam注解,否则会报错
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(categoryService.queryCategoryByIds(ids));
    }


 /*   @GetMapping("bid/{bid}")
    @ApiOperation(value = "根据品牌Id查询品牌及分类信息")
    public ResponseEntity<BrandVo> queryBrandVoByBrandId(@PathVariable(name = "bid") Long brandId){
        return ResponseEntity.ok(categoryService.queryBrandVoByBrandId(brandId));
    }
*/


    @GetMapping("bid/{bid}")
    @ApiOperation(value = "根据品牌Id查询品牌及分类信息")
    public ResponseEntity<List<Category>> queryCatoriesByBrandId(@PathVariable(name = "bid") Long brandId){
        return ResponseEntity.ok(categoryService.queryCatoriesByBrandId(brandId));
    }
}
