package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;


    public List<Category> queryCategoryListByPid(Long pid) {
        Category t = new Category();
        t.setParentId(pid);
        List<Category> categories = categoryMapper.select(t);
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        return categories;
    }


    public List<Category> queryCategoryByIds(List<Long> ids){
        List<Category> categories = categoryMapper.selectByIdList(ids);

        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        return categories;
    }


    public List<Category> queryCatoriesByBrandId(Long brandId) {

        List<Category> categories = categoryMapper.selectCategoryByBid(brandId);
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        return categories;
    }

    public BrandVo queryBrandVoByBrandId(Long brandId) {

        Brand brand = brandMapper.selectByPrimaryKey(brandId);
        if (brand==null) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }
        BrandVo brandVo = new BrandVo();
        brandVo.setId(brand.getId());
        brandVo.setName(brand.getName());
        brandVo.setLetter(brand.getLetter());
        brandVo.setImage(brand.getImage());
        List<Long> categories = categoryMapper.selectCidsByBrandId(brandId);
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOUND);
        }
        brandVo.setCategories(categories);
        return brandVo;
    }
}
