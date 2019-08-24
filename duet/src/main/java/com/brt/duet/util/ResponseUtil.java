package com.brt.duet.util;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ResponseUtil {
	
    public static final String JSON_SUCCESS = "success";
	public static final String JSON_MESSAGE = "message";
	public static final String JSON_DATA = "data";
    
    public static JSONObject responseResult(boolean success, String message, Object data) {
		JSONObject result = new JSONObject();
		result.put(JSON_SUCCESS, success);
		if (StringUtils.isNotBlank(message)) {
			result.put(JSON_MESSAGE, message);
		} else {
			result.put(JSON_MESSAGE, success ? "succ" : "fail");
		}
		if (data != null) {
			//处理时间格式
			result.put(JSON_DATA, JSON.parse(JSON.toJSONStringWithDateFormat(data, "yyyy-MM-dd HH:mm:ss")));	
		}
		return result;
	}
}
