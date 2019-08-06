package com.leyou.page.web;

import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class PageController {
    @Autowired
    private PageService pageService;

    @GetMapping("{id}.html")
    public String index(@PathVariable("id") Long id, Model model){
        //通过SpuId去获取数据
        Map<String, Object> map = pageService.loadData(id);
        model.addAllAttributes(map);
        return "item";
    }



}
