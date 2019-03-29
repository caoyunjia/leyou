package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnums;
import lombok.Data;
import lombok.Getter;

@Data
public class ExceptionResult {
    private  int code;
    private String message;
    private long timestamp;

    public ExceptionResult(ExceptionEnums exceptionEnums){
        this.code = exceptionEnums.getCode();
        this.message = exceptionEnums.getMsg();
        this.timestamp = System.currentTimeMillis();
    }

}
