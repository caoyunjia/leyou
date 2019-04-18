package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;



@Table(name = "tb_spu_detail")
@Data
public class SpuDetail {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long spuId;  //Spu的id
    private String packingList;
    private String afterService;  //售后服务
    private String genericSpec; //
    private String specialSpec; //
}
