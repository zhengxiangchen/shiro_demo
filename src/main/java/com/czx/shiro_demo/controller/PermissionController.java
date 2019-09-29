package com.czx.shiro_demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.entity.PermissionEntity;
import com.czx.shiro_demo.service.IPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限控制器
 */
@Slf4j
@RequestMapping("permission")
@RestController
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;


    /**
     * 新增权限
     * @return
     */
    @PostMapping("add")
    public JSONObject addPermission(@RequestBody PermissionEntity permissionEntity){
        return permissionService.add(permissionEntity);
    }

}
