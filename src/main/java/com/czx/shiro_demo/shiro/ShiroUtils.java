package com.czx.shiro_demo.shiro;

import com.czx.shiro_demo.entity.UserEntity;
import org.apache.shiro.SecurityUtils;

public class ShiroUtils {

    /**
     * 获取当前用户
     * @return
     */
    public static UserEntity getCurrentUser(){
        return (UserEntity)SecurityUtils.getSubject().getPrincipal();
    }
}
