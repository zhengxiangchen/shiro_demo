package com.czx.shiro_demo.tools;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ResponseFormat {

	private static Map<String,String> messageMap = new HashMap<>();
	
	static {
		messageMap.put("200", "操作成功");
		messageMap.put("300", "用户未登录");
		messageMap.put("400", "登录鉴权失败");
		messageMap.put("500", "操作失败");
		messageMap.put("600", "参数错误");
		messageMap.put("700", "无操作权限");


		messageMap.put("10001", "用户名已存在");

		messageMap.put("20001", "权限名称已存在");

		messageMap.put("30001", "角色名称已存在");

	}
	
	public static JSONObject buildResponseMap(String status, JSONObject response) {
		response.put("code", status);
		response.put("msg", messageMap.get(status));
		log.info("接口请求返回---------->[{}]", response);
		return response;
	}
	
	
}
