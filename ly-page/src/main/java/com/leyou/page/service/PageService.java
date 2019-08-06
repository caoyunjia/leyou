package com.leyou.page.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PageService
{

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;
    /**
     *加载thymeleaf页面所需要的数据
     * @param id
     * @return
     */
    public Map loadData(Long id){
        HashMap<String, Object> model = new HashMap<>();
        Spu spu = goodsClient.querySpuById(id);
        List<Category> categories = categoryClient.queryCategoryListByPid(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        List<SpecGroup> specs = specificationClient.queryGroupAndParamByCid(spu.getCid3());
        model.put("title", spu.getTitle());
        model.put("subTitle",spu.getSubTitle());
        model.put("detail", spu.getSpuDetail());
        model.put("brand", brand);
        model.put("categories",categories);
        model.put("skus",spu.getSkus());
        model.put("specs",specs);
        return model;
    }

    public void createHtml(Long spuId){
        Context context = new Context();
        context.setVariables(loadData(spuId));
        File dest = new File("/home/elk/xx", spuId + ".html");
        if(dest.exists()){
            dest.delete();
        }
        try(PrintWriter writer = new PrintWriter(dest, "UTF-8")){
            templateEngine.process("item",context ,writer);
        }catch (Exception e){
            log.error("静态页服务生成静态页异常",e);
        }
    }

    public void deletePage(Long spuId) {
        File dest = new File("/home/elk/xx", spuId + ".html");
        if(dest.exists()){
            dest.delete();
        }
    }
}
