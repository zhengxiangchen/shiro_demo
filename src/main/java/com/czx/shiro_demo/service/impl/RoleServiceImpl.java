package com.czx.shiro_demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.entity.RoleEntity;
import com.czx.shiro_demo.repository.RoleRepository;
import com.czx.shiro_demo.service.IRoleService;
import com.czx.shiro_demo.tools.ResponseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色接口实现类
 */
@Slf4j
@Service
public class RoleServiceImpl implements IRoleService {


    @Autowired
    private RoleRepository roleRepository;


    /**
     * 新增角色
     * @param roleEntity
     * @return
     */
    @Override
    public JSONObject add(RoleEntity roleEntity) {
        String name = roleEntity.getName();
        if(name == null || name.trim().length() <= 0){
            log.info("新增角色参数错误------>[{}]", JSONObject.toJSONString(roleEntity));
            return ResponseFormat.buildResponseJson("600", new JSONObject());
        }

        RoleEntity role = roleRepository.findByName(name);

        if(role != null){
            return ResponseFormat.buildResponseJson("30001", new JSONObject());
        }

        roleRepository.save(roleEntity);
        return ResponseFormat.buildResponseJson("200", new JSONObject());
    }
}
