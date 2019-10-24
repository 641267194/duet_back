package com.brt.duet.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 方杰
 * @date 2019年9月16日
 * @description ip工具类
 */
public class IPUtil {
	/**
	 * @author 方杰
	 * @date 2019年9月16日
	 * @param request
	 * @return 请求来源ip
	 * @description 获取请求来源的ip地址
	 */
	public static String getHostIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("Proxy-Client-IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("WL-Proxy-Client-IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("HTTP_CLIENT_IP");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getRemoteAddr();
	        }
	    } else if (ip.length() > 15) {
	        String[] ips = ip.split(",");
	        for (int index = 0; index < ips.length; index++) {
	            String strIp = ips[index];
	            if (!("unknown".equalsIgnoreCase(strIp))) {
	                ip = strIp;
	                break;
	            }
	        }
	    }
	    // 如果是本机
	    if (ip.equals("127.0.0.1")) {
            // 根据网卡取本机配置的IP
            InetAddress inet = null;
            try {
                inet = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            ip = inet.getHostAddress();
        }

	    return ip;
	}
}
