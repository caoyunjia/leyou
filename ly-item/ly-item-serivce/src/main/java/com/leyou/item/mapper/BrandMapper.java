package com.leyou.item.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface BrandMapper extends BaseMapper<Brand,Long> {

    @Insert("insert into tb_category_brand(category_id,brand_id)values(#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid")Long cid, @Param("bid") Long bid);

    @Select("SELECT b.* FROM tb_category_brand a INNER JOIN tb_brand b ON a.brand_id=b.id  where a.category_id=#{cid}")
    List<Brand> queryBrandsByCid(@Param("cid")Long cid);
}