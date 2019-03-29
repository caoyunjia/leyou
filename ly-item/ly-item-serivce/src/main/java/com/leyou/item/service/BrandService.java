package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.vo.BrandVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.List;

@Service
@Transactional
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {

        PageHelper.startPage(page, rows);
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        }
        //如果没有排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause=sortBy+(desc?" DESC":" ASC");
            example.setOrderByClause(orderByClause);
        }
        List<Brand> brands = brandMapper.selectByExample(example);

        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        //如果集合为空
        if(CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }

        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 保存品牌
     * @param brandVo
     */
    public void saveBrand(BrandVo brandVo) {
        brandVo.setId(null);

        Brand brand = new Brand();
        brand.setId(null);
        brand.setName(brandVo.getName());
        brand.setLetter(brandVo.getLetter());
        brandVo.setImage(brandVo.getImage());
        int count = brandMapper.insert(brand);
        if(count!=1){
            throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
        }
        //新增中间表
        brandVo.getCategories().forEach(cid->{
            int i = brandMapper.insertCategoryBrand(cid, brandVo.getId());
            if(i!=1){
                throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
            }
        });



    }
}
