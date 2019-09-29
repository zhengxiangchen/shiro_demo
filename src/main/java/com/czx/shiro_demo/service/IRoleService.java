package com.czx.shiro_demo.service;


import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.entity.RoleEntity;

/**
 * 角色接口
 */
public interface IRoleService {
    JSONObject add(RoleEntity roleEntity);
}
