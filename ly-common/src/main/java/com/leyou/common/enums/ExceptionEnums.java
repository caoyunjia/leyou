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
    BRAND_SAVE_ERROR(500,"品牌新增失败" ),
    SPEC_GROUP_SAVE_ERROR(500,"商品规则组不存在" ),
    UPLOAD_FILE_ERROR(500,"文件上传失败" ),
    INVALID_FILE_ERROR(500,"无效的文件类型" );
    private int code;
    private String msg;



}
