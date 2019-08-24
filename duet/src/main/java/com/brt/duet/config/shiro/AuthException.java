package com.brt.duet.config.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSON;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 处理权限不足问题
 */
@ControllerAdvice
public class AuthException {
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param res
	 * @param e
	 * @throws IOException 
	 * @description 处理访问方法时权限不足问题
	 */
	@ExceptionHandler(value = UnauthorizedException.class)
    public void AuthcErrorHandler(HttpServletResponse res, Exception e) throws IOException {
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setContentType("application/json; charset=utf-8");
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        PrintWriter writer = res.getWriter();
        Map<String, Object> map= new HashMap<>();
        map.put("success", false);
        map.put("message", "权限不足");
        writer.write(JSON.toJSONString(map));
        writer.close();
    }
}
