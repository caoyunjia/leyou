package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    @GetMapping(value = "/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable(value = "cid")Long cid){

        List<SpecGroup> specGroups=specificationService.queryGroupsByCid(cid);
        return ResponseEntity.ok(specGroups);
    }

    @GetMapping(value = "/params/{gid}")
    public ResponseEntity<List<SpecParam>> queryParamsByGid(@PathVariable(value = "gid")Long gid){

        List<SpecParam> specParams=specificationService.queryParamsByGid(gid);
        return ResponseEntity.ok(specParams);
    }

    /**
     * 新增参数组
     * @param specGroup
     * @return
     */
    @PostMapping(value = "/group")
    public ResponseEntity<Void> saveSpecGroup(@RequestBody SpecGroup specGroup){
        specificationService.saveSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 新增参数
     * @param specParam
     * @return
     */
    @PostMapping(value = "/param")
    public ResponseEntity<Void> saveSpecParam(@RequestBody SpecParam specParam){
        specificationService.saveSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> queryParamsByCid(Long cid){

        List<SpecParam> specParams=specificationService.queryParamsByCid(cid);
        return ResponseEntity.ok(specParams);
    }

}
