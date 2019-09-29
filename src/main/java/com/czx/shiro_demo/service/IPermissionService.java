package com.czx.shiro_demo.service;

import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.entity.PermissionEntity;

/**
 * 权限接口
 */
public interface IPermissionService {
    JSONObject add(PermissionEntity permissionEntity);
}
