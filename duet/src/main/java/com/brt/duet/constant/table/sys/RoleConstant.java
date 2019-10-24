package com.brt.duet.constant.table.sys;

import com.brt.duet.constant.RegexConstant;
import com.brt.duet.constant.TableColumn;

/**
 * @description 角色表
 */
public interface RoleConstant {
    String TABLE_NAME = "sys_role";
    /**
     * @description 角色id
     */
    TableColumn ID = new TableColumn("id", "sys_role.id", 1, RegexConstant.ID_NOT_NULL);
    /**
     * @description 角色名
     */
    TableColumn NAME = new TableColumn("name", "sys_role.name", 12, "^[\\s\\S]{0,255}$");
    /**
     * @description 角色编码
     */
    TableColumn CODE = new TableColumn("code", "sys_role.code", 12, "^[A-Za-z0-9_:]{1,255}$");
    /**
     * @description 创建时间
     */
    TableColumn CREATE_AT = new TableColumn("createAt", "sys_role.create_at", 93, RegexConstant.DATETIME);
    /**
     * @description 修改时间
     */
    TableColumn UPDATE_AT = new TableColumn("updateAt", "sys_role.update_at", 93, RegexConstant.DATETIME);
    /**
     * @description 模块信息
     */
    TableColumn MODULES = new TableColumn("modules", "sys_role.id AS modules", -100, null);
}
