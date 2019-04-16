package com.leyou.item.web;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryListByPid(List<Long> ids) {

        return ResponseEntity.ok(categoryService.queryCategoryListByIds(ids));
    }
}
