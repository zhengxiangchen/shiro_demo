package com.czx.shiro_demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.entity.UserEntity;
import com.czx.shiro_demo.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;


    /**
     * 新增用户
     * @param userEntity
     * @return
     */
    @PostMapping("add")
    public JSONObject addUser(@RequestBody UserEntity userEntity){
        return userService.add(userEntity);
    }




}
