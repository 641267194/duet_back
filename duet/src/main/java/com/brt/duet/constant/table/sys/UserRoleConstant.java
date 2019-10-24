package com.brt.duet.constant.table.sys;

import com.brt.duet.constant.RegexConstant;
import com.brt.duet.constant.TableColumn;

/**
 * @description 用户角色关联表
 */
public interface UserRoleConstant {
    String TABLE_NAME = "sys_user_role";
    /**
     * @description 用户id
     */
    TableColumn USER_ID = new TableColumn("userId", "sys_user_role.user_id", 1, RegexConstant.ID_NOT_NULL);
    /**
     * @description 角色id
     */
    TableColumn ROLE_ID = new TableColumn("roleId", "sys_user_role.role_id", 1, RegexConstant.ID_NOT_NULL);
    /**
     * @description 创建时间
     */
    TableColumn CREATE_AT = new TableColumn("createAt", "sys_user_role.create_at", 93, RegexConstant.DATETIME);
}
