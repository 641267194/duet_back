package com.brt.duet.constant.table.sys;

import com.brt.duet.constant.RegexConstant;
import com.brt.duet.constant.TableColumn;

/**
 * @description 角色模块关联表
 */
public interface RoleModuleConstant {
    String TABLE_NAME = "sys_role_module";
    /**
     * @description 角色id
     */
    TableColumn ROLE_ID =  new TableColumn("roleId", "sys_role_module.role_id", 1, RegexConstant.ID_NOT_NULL);
    /**
     * @description 模块id
     */
    TableColumn MODULE_ID =  new TableColumn("moduleId", "sys_role_module.module_id", 1, RegexConstant.ID_NOT_NULL);
    /**
     * @description 创建时间
     */
    TableColumn CREATE_AT =  new TableColumn("createAt", "sys_role_module.create_at", 93, RegexConstant.DATETIME);
}
