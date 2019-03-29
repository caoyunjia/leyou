package com.leyou.item.mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import com.leyou.item.pojo.Brand;

import java.util.List;


public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand(category_id,brand_id)values(#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid")Long cid, @Param("bid") Long bid);
}