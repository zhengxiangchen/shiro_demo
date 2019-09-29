package com.czx.shiro_demo.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.czx.shiro_demo.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 单点登录
 * 拦截器---每个操作都会经过拦截器----获取到request的header中的token
 * 查询token在redis中是否存在--存在则表示用户已登录状态(更新对应的redis中的存储时间)---不存在则表示用户登录已失效
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;



    private static List<String> IGNORE_URI_LIST = new ArrayList<>();
    static {
        IGNORE_URI_LIST.add("/login");
        IGNORE_URI_LIST.add("/logout");
    }

    /**
     * 预处理回调方法，实现处理器的预处理
     * 返回值：true表示继续流程；false表示流程中断，不会继续调用其他的拦截器或处理器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();

        //如果url在忽略列表中则直接返回true
        if(IGNORE_URI_LIST.contains(url)){
            log.info("拦截器----->[{}]直接放行", url);
            return true;
        }
        String token = request.getHeader("token");
        if(token == null || token.length() <= 0){
            log.info("请求中无token值--------------->直接拦截");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "401");
            jsonObject.put("msg", "token验证失败");
            returnJson(response, JSONObject.toJSONString(jsonObject));
            return false;
        }

        boolean hasToken = redisUtils.hasKey(token);
        if(hasToken){
            log.info("token验证成功------->拦截器放行");
            //获取key值的过期时间
            long expireTime = redisUtils.getExpire(token);
            //如果小于60秒则重新设置过期时间
            if(expireTime <= 60){
                String username = (String)redisUtils.get(token);redisUtils.expire(username, 60 * 30);
                redisUtils.expire(token, 60 * 30);

            }
            return true;
        }else{
            log.info("token验证失败------->登录状态已失效,重新登录!");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "401");
            jsonObject.put("msg", "token验证失败");
            returnJson(response, JSONObject.toJSONString(jsonObject));
            return false;
        }
    }

    /**
     * 后处理回调方法，实现处理器（controller）的后处理，但在渲染视图之前
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，
     * 如性能监控中我们可以在此记录结束时间并输出消耗时间，
     * 还可以进行一些资源清理，类似于try-catch-finally中的finally，
     * 但仅调用处理器执行链中
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {


    }


    /**
     * 返回json数据给前端
     * @param response
     * @param json
     * @throws Exception
     */
    private void returnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
            log.error("response error",e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
