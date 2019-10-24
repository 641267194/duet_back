package com.brt.duet.aop.log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.brt.duet.constant.table.sys.LogConstant;
import com.brt.duet.constant.table.sys.UserConstant;
import com.brt.duet.service.sys.LogService;
import com.brt.duet.util.IDUtil;
import com.brt.duet.util.IPUtil;
import com.brt.duet.util.SessionUtil;

/**
 * @author 方杰
 * @date 2019年9月16日
 * @description 系统日志收集Aspect
 */
@Aspect
@Component
public class SystemLogAspect {
	
	@Autowired
	private LogService logService;
	
	/**
	 * @author 方杰
	 * @date 2019年9月16日 
	 * @description 声明切入点
	 */
	@Pointcut("@annotation(com.brt.duet.aop.log.EnableSystemLog)")
	public void systemLog() {}
	
	/**
	 * @author 方杰
	 * @date 2019年9月16日
	 * @param joinPoint
	 * @param enableSystemLog 
	 * @description 正常日志记录
	 */
	@AfterReturning(pointcut = "systemLog()&&@annotation(enableSystemLog)")
	public void afterReturning(JoinPoint joinPoint, EnableSystemLog enableSystemLog) {
		Map<String, Object> mapInsert = getCommonData(joinPoint, enableSystemLog);
		mapInsert.put(LogConstant.SUCCESS.getTableColumn(), 1);
		logService.insert(mapInsert);
	}
	
	/**
	 * @author 方杰
	 * @date 2019年9月16日
	 * @param joinPoint
	 * @param enableSystemLog 
	 * @description 异常日志记录
	 */
	@AfterThrowing(pointcut = "systemLog()&&@annotation(enableSystemLog)", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, EnableSystemLog enableSystemLog, Exception e) {
		Map<String, Object> mapInsert = getCommonData(joinPoint, enableSystemLog);
		mapInsert.put(LogConstant.SUCCESS.getTableColumn(), 0);
		// 异常信息收集
		mapInsert.put(LogConstant.EXCEPTION.getTableColumn(), getExceptionDetail(e));
		logService.insert(mapInsert);
	}
	
	/**
	 * @author 方杰
	 * @date 2019年9月16日
	 * @param joinPoint
	 * @param enableSystemLog
	 * @return 需要插入数据库的部分数据
	 * @description 获取正常异常日志的共同数据
	 */
	private Map<String, Object> getCommonData(JoinPoint joinPoint, EnableSystemLog enableSystemLog) {
        Map<String, Object> mapInsert = new HashMap<String, Object>();
		
		mapInsert.put(LogConstant.ID.getTableColumn(), IDUtil.getUUID());
		// 方法信息收集
		mapInsert.put(LogConstant.OPERATION_EVENT.getTableColumn(), enableSystemLog.operationEvent());
		mapInsert.put(LogConstant.OPERATION_TYPE.getTableColumn(), enableSystemLog.operationType());
		// 用户信息收集
		Map<String, Object> userSession = SessionUtil.getUserSession();
		if (userSession != null) {
			mapInsert.put(LogConstant.OPERATION_USER_ID.getTableColumn(), userSession.get(UserConstant.ID.getKey()));
			mapInsert.put(LogConstant.OPERATION_USERNAME.getTableColumn(), userSession.get(UserConstant.USERNAME.getKey()));
		}
		// 请求信息收集
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		mapInsert.put(LogConstant.REMOTE_ADDR.getTableColumn(), IPUtil.getHostIP(request));
		mapInsert.put(LogConstant.USER_AGENT.getTableColumn(), request.getHeader("user-agent"));
		mapInsert.put(LogConstant.REQUEST_URI.getTableColumn(), request.getRequestURI());
		mapInsert.put(LogConstant.METHOD.getTableColumn(), request.getMethod());
		mapInsert.put(LogConstant.PARAMS.getTableColumn(), Arrays.toString(joinPoint.getArgs()));
		
		return mapInsert;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年9月16日
	 * @param e
	 * @return 异常的信息
	 * @description 获取异常的信息
	 */
	private String getExceptionDetail(Exception e) {  
        StringBuffer stringBuffer = new StringBuffer(e.toString() + "\n");  
        StackTraceElement[] messages = e.getStackTrace();  
        int length = messages.length;  
        for (int i = 0; i < length; i++) {  
            stringBuffer.append("\t" + messages[i].toString() + "\n");  
        }  
        return stringBuffer.toString();  
    } 

}
