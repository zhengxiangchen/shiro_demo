package com.czx.shiro_demo.shiro;

import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.tools.ResponseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionResolver{

    /**
     * 获取无权限异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public JSONObject unauthorizedExceptionHandler() {
        return ResponseFormat.buildResponseMap("700", new JSONObject());
    }


}
