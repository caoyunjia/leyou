package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroups = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(specGroups)) {
            throw new LyException(ExceptionEnums.SPEC_GROUP_NOT_FOUND);
        }
        return specGroups;
    }

    public void saveSpecGroup(SpecGroup specGroup) {
        specGroup.setId(null);
        int count = specGroupMapper.insert(specGroup);
        if(count!=1){
            throw new LyException(ExceptionEnums.SPEC_GROUP_SAVE_ERROR);
        }
    }

    public List<SpecParam> queryParamsByGid(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        List<SpecParam> specParams = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(specParams)) {
            throw new LyException(ExceptionEnums.SPEC_PARAM_NOT_FOUND);
        }
        return specParams;
    }

    public void saveSpecParam(SpecParam specParam) {
        specParam.setId(null);
        int count = specParamMapper.insert(specParam);
        if(count!=1){
            throw new LyException(ExceptionEnums.SPEC_PARAM_SAVE_ERROR);
        }
    }

    public List<SpecParam> queryParamsByCid(Long cid) {

        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        List<SpecParam> specParams = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(specParams)) {
            throw new LyException(ExceptionEnums.SPEC_PARAM_NOT_FOUND);
        }
        return specParams;
    }

    public List<SpecGroup> queryGroupAndParamByCid(Long cid) {
        //查询规格组
        List<SpecGroup> specGroups = queryGroupsByCid(cid);

        List<SpecParam> specParams = queryParamsByCid(cid);
        HashMap<Long, List<SpecParam>> paramMap = new HashMap<>();
        specParams.forEach(specParam -> {
            if(!paramMap.containsKey(specParam.getGroupId())){
                paramMap.put(specParam.getGroupId(), new ArrayList<>());
            }
            paramMap.get(specParam.getGroupId()).add(specParam);
        });
        specGroups.forEach(group -> group.setParams(paramMap.get(group.getId())));
        return specGroups;

    }
}
