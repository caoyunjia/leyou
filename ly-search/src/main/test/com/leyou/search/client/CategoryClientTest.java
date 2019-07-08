package com.leyou.search.client;

import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpecParam;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryClientTest {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecificationClient specificationClient;

    @Test
    public void queryCategoryListByPid() {

        List<Category> categories = categoryClient.queryCategoryListByPid(Arrays.asList(1L, 2L));

        Assert.assertEquals(2, categories.size());
        for (Category category : categories) {
            System.out.println(category);
        }
    }


    @Test
    public void queryParamsByCid() {
        List<SpecParam> specParams = specificationClient.queryParamsByCid(76L);
        for (SpecParam specParam : specParams) {
            System.out.println(specParam.getName());
        }


    }
}