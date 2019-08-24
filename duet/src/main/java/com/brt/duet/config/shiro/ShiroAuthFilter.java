package com.brt.duet.config.shiro;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.alibaba.fastjson.JSON;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 重写shiro过滤器,没有登录返回JSON
 */
public class ShiroAuthFilter extends FormAuthenticationFilter {

    public ShiroAuthFilter() {
        super();
    }

    /**
     * @author 方杰
     * @date 2019年7月22日
     * @param request
     * @param response
     * @param mappedValue
     * @return 
     * @description 
     * @see org.apache.shiro.web.filter.authc.AuthenticatingFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object) 
     */
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //Always return true if the request's method is OPTIONS
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * @author 方杰
     * @date 2019年7月22日
     * @param request
     * @param response
     * @return
     * @throws Exception 
     * @description 处理没有登录直接返回json,(前后端分离,不直接跳转登录页,由前端处理)
     * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse) 
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse)response;
        res.setHeader("Access-Control-Allow-Origin", "true");
        res.setContentType("application/json; charset=utf-8");
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = res.getWriter();
        Map<String, Object> map= new HashMap<>();
        map.put("success", false);
        map.put("message", "请先登录");
        writer.write(JSON.toJSONString(map));
        writer.close();
        return false;
    }
}
