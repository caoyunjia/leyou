package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.pojo.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

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
}
