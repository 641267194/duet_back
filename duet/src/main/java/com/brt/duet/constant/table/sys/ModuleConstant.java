package com.brt.duet.constant.table.sys;

import com.brt.duet.constant.RegexConstant;
import com.brt.duet.constant.TableColumn;

/**
 * @description 模块表
 */
public interface ModuleConstant {
    String TABLE_NAME = "sys_module";
    /**
     * @description 主键id
     */
    TableColumn ID =  new TableColumn("id", "sys_module.id", 1, RegexConstant.ID_NOT_NULL);
    /**
     * @description 模块名称
     */
    TableColumn NAME =  new TableColumn("name", "sys_module.name", 12, "^[\\s\\S]{0,100}$");
    /**
     * @description 模块标题
     */
    TableColumn TITLE =  new TableColumn("title", "sys_module.title", 12, "^[\\s\\S]{0,100}$");
    /**
     * @description 模块编码
     */
    TableColumn CODE =  new TableColumn("code", "sys_module.code", 12, "^[A-Za-z0-9_]{1,8}$");
    /**
     * @description 模块的父模块id
     */
    TableColumn PARENT_ID =  new TableColumn("parentId", "sys_module.parent_id", 1, RegexConstant.ID_NULL);
    /**
     * @description 模块连接地址
     */
    TableColumn URI =  new TableColumn("uri", "sys_module.uri", 12, "^[\\s\\S]{0,255}$");
    /**
     * @description 模块图标
     */
    TableColumn ICON =  new TableColumn("icon", "sys_module.icon", 12, "^[\\s\\S]{0,255}$");
    /**
     * @description 模块的排序
     */
    TableColumn SORT =  new TableColumn("sort", "sys_module.sort", 4, "^[0-9]{1,10}$");
    /**
     * @description 模块是否是菜单,0否 1是
     */
    TableColumn IS_MENU =  new TableColumn("isMenu", "sys_module.is_menu", -7, RegexConstant.BOOLEAN);
    /**
     * @description 创建时间
     */
    TableColumn CREATE_AT =  new TableColumn("createAt", "sys_module.create_at", 93, RegexConstant.DATETIME);
    /**
     * @description 更新时间
     */
    TableColumn UPDATE_AT =  new TableColumn("updateAt", "sys_module.update_at", 93, RegexConstant.DATETIME);
}
