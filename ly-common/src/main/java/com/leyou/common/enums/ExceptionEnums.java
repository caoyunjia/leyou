package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  ExceptionEnums {
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    BRAND_NOT_FOUND(404,"品牌不存在"),
    CATEGORY_NOT_FOUND(400, "分类不存在"),
    SPEC_GROUP_NOT_FOUND(400,"商品规格组不存在" ),
    SPEC_PARAM_NOT_FOUND(400,"商品规格参数不存在" ),
    GOODS_NOT_FOUND(404,"商品不存在"),
    GOODS_DETAIL_NOT_FOUND(404,"商品详情不存在"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU不存在"),
    GOODS_SPU_NOT_FOUND(404,"商品SPU不存在"),
    GOODS_STOCK_NOT_FOUND(404,"商品STOCK不存在"),
    BRAND_SAVE_ERROR(500,"品牌新增失败" ),
    GOODS_SAVE_ERROR(500,"商品新增失败" ),
    GOODS_UPDATE_ERROR(500,"商品修改失败" ),
    SPEC_GROUP_SAVE_ERROR(500,"商品规格组新增失败" ),
    SPEC_PARAM_SAVE_ERROR(500,"商品规格参数新增失败" ),
    UPLOAD_FILE_ERROR(500,"文件上传失败" ),
    INVALID_FILE_ERROR(500,"无效的文件类型" ),
    INVALID_PARAM_ERROR(400,"参数类型有误" );
    private int code;
    private String msg;



}
