package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {

    @GetMapping("spec/params/")
    List<SpecParam> queryParamsByCid(@RequestParam(value = "cid") Long cid);

    @GetMapping("spec/group")
    List<SpecGroup> queryGroupAndParamByCid(@RequestParam(value = "cid") Long cid);

}
