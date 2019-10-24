package com.brt.duet.aop.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 方杰
 * @date 2019年9月16日
 * @description 系统日志收集注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnableSystemLog {
	
	/**
	 * @author 方杰
	 * @date 2019年9月16日
	 * @return 操作事件
	 * @description 操作事件
	 */
	public String operationEvent() default "";
	
	/**
	 * @author 方杰
	 * @date 2019年9月16日
	 * @return 操作类型
	 * @description 操作类型
	 */
	public String operationType() default "";
}
