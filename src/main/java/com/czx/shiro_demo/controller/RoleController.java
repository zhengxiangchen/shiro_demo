package com.czx.shiro_demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.entity.RoleEntity;
import com.czx.shiro_demo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色控制器
 */
@RequestMapping("role")
@RestController
public class RoleController {

    @Autowired
    private IRoleService roleService;


    /**
     * 新增角色
     * @param roleEntity
     * @return
     */
    @PostMapping("add")
    public JSONObject addRole(@RequestBody RoleEntity roleEntity){
        return roleService.add(roleEntity);
    }


}
