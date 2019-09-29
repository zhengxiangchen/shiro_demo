package com.czx.shiro_demo.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.entity.UserEntity;
import com.czx.shiro_demo.shiro.ShiroUtils;
import com.czx.shiro_demo.tools.ResponseFormat;
import com.czx.shiro_demo.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录登出控制器
 */
@Slf4j
@RequestMapping("/")
@RestController
public class LoginController {


    @Autowired
    private RedisUtils redisUtils;


    /**
     * 用户登录
     * @param userEntity
     * @return
     */
    @PostMapping("login")
    public JSONObject login(@RequestBody UserEntity userEntity){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userEntity.getUsername(), userEntity.getPassword());

        try{
            subject.login(token);

            //单点登录--操作
            String username = userEntity.getUsername();
            String random_token = IdUtil.objectId();

            log.info("[{}]token[{}]已存入redis缓存", username, random_token);
            String before_token = (String)redisUtils.get(username);
            if(before_token != null){
                redisUtils.del(before_token);
            }

            redisUtils.set(username, random_token, 60 * 30);
            redisUtils.set(random_token, username, 60 * 30);
            //单点登录--操作

            log.info("[{}]-登录成功", username);

            JSONObject result_obj = new JSONObject();
            result_obj.put("token", random_token);
            return ResponseFormat.buildResponseMap("200", result_obj);
        }catch (Exception e){
            log.info("[{}]-登录失败------->{}",userEntity.getUsername(), e.getMessage());
        }

        return ResponseFormat.buildResponseMap("400", new JSONObject());
    }


    /**
     * 退出登录
     * @return
     */
    @PostMapping("logout")
    public JSONObject logout(){
        Subject subject = SecurityUtils.getSubject();
        UserEntity userEntity = (UserEntity) subject.getPrincipal();
        if(userEntity != null){
            log.info("[{}]已登出", userEntity.getUsername());
            //清理redis中的对应缓存
            String token = (String)redisUtils.get(userEntity.getUsername());
            redisUtils.del(token, userEntity.getUsername());
            log.info("redis缓存已清理完成");
        }
        subject.logout();
        return ResponseFormat.buildResponseMap("200", new JSONObject());
    }


    /**
     * 获取当前用户
     * @return
     */
    @RequiresPermissions("user:view")
    @GetMapping("getCurrentUser")
    public JSONObject getCurrentUser(){
        JSONObject resultObj = new JSONObject();
        UserEntity userEntity = ShiroUtils.getCurrentUser();
        resultObj.put("user", JSONObject.toJSONString(userEntity));
        return ResponseFormat.buildResponseMap("200", resultObj);
    }


    /**
     * shiro检测未登录---返回给前端
     * @return
     */
    @GetMapping("unAuthor")
    public JSONObject unAuthor(){
        return ResponseFormat.buildResponseMap("300", new JSONObject());
    }
}
