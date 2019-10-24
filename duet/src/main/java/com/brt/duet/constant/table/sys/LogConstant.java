package com.brt.duet.constant.table.sys;

import com.brt.duet.constant.RegexConstant;
import com.brt.duet.constant.TableColumn;

/**
 * @description 系统日志表
 */
public interface LogConstant {
    String TABLE_NAME = "sys_log";
    /**
     * @description 主键id
     */
    TableColumn ID = new TableColumn("id", "sys_log.id", 1, RegexConstant.ID_NOT_NULL);
    /**
     * @description 操作事件
     */
    TableColumn OPERATION_EVENT = new TableColumn("operationEvent", "sys_log.operation_event", 12, "^[\\s\\S]{0,255}$");
    /**
     * @description 操作类型
     */
    TableColumn OPERATION_TYPE = new TableColumn("operationType", "sys_log.operation_type", 12, "^[\\s\\S]{0,255}$");
    /**
     * @description 是否成功 1：成功 0：失败
     */
    TableColumn SUCCESS = new TableColumn("success", "sys_log.success", -7, RegexConstant.BOOLEAN);
    /**
     * @description 操作者id
     */
    TableColumn OPERATION_USER_ID = new TableColumn("operationUserId", "sys_log.operation_user_id", 1, RegexConstant.ID_NOT_NULL);
    /**
     * @description 操作者用户名
     */
    TableColumn OPERATION_USERNAME = new TableColumn("operationUsername", "sys_log.operation_username", 12, "^[A-Za-z0-9_]{4,50}$");
    /**
     * @description 操作IP地址
     */
    TableColumn REMOTE_ADDR = new TableColumn("remoteAddr", "sys_log.remote_addr", 12, "^[\\s\\S]{0,255}$");
    /**
     * @description 请求来源类型
     */
    TableColumn USER_AGENT = new TableColumn("userAgent", "sys_log.user_agent", 12, "^[\\s\\S]{0,255}$");
    /**
     * @description 请求URI
     */
    TableColumn REQUEST_URI = new TableColumn("requestUri", "sys_log.request_uri", 12, "^[\\s\\S]{0,4000}$");
    /**
     * @description 请求类型
     */
    TableColumn METHOD = new TableColumn("method", "sys_log.method", 12, "^[A-Za-z]{0,10}$");
    /**
     * @description 请求参数
     */
    TableColumn PARAMS = new TableColumn("params", "sys_log.params", -1, "^[\\s\\S]*$");
    /**
     * @description 异常信息
     */
    TableColumn EXCEPTION = new TableColumn("exception", "sys_log.exception", -1, "^[\\s\\S]*$");
    /**
     * @description 操作时间
     */
    TableColumn CREATE_AT = new TableColumn("createAt", "sys_log.create_at", 93, RegexConstant.DATETIME);
}
