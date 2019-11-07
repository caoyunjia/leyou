package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>,IdListMapper<Category,Long> {

    @Select("SELECT b.* FROM tb_category_brand a INNER JOIN tb_category b ON a.category_id=b.id  where a.brand_id=#{bid}")
    List<Category> selectCategoryByBid(@Param("bid")Long bid);
    @Select("SELECT a.category_id  FROM tb_category_brand a where brand_id=#{bid}")
    List<Long> selectCidsByBrandId(@Param("bid")Long bid);
}
