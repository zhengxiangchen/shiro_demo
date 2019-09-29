package com.czx.shiro_demo.service;

import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.entity.UserEntity;

/**
 * 用户接口
 */
public interface IUserService {
    JSONObject add(UserEntity userEntity);
}
