package com.czx.shiro_demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.entity.PermissionEntity;
import com.czx.shiro_demo.repository.PermissionRepository;
import com.czx.shiro_demo.service.IPermissionService;
import com.czx.shiro_demo.tools.ResponseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限接口实现类
 */
@Slf4j
@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;


    /**
     * 新增权限
     * @param permissionEntity
     * @return
     */
    @Override
    public JSONObject add(PermissionEntity permissionEntity) {
        String name = permissionEntity.getName();
        String key = permissionEntity.getKey();
        if(name == null || name.trim().length() <= 0 || key == null || key.trim().length() <= 0){
            log.info("新增权限参数错误-------->[{}]", JSONObject.toJSONString(permissionEntity));
            return ResponseFormat.buildResponseJson("600", new JSONObject());
        }

        PermissionEntity permission = permissionRepository.findByName(permissionEntity.getName());

        //权限名称重复
        if(permission != null){
            return ResponseFormat.buildResponseJson("20001", new JSONObject());
        }

        permissionRepository.save(permissionEntity);

        return ResponseFormat.buildResponseJson("200", new JSONObject());
    }
}
