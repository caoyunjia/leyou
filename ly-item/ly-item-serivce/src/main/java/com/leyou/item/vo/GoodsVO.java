package com.leyou.item.vo;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import lombok.Data;

import java.util.List;

@Data
public class GoodsVO  extends Spu{


    private SpuDetail spuDetail;

    private List<Sku> skus;

}
