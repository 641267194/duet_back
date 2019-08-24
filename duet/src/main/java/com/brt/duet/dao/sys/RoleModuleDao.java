package com.brt.duet.dao.sys;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 角色模块关联表Dao
 */
public interface RoleModuleDao {

    int insert(@Param("mapInsert") Map<String, Object> mapInsert);

    int delete(@Param("mapWhere") Map<String, List<Map<String, Object>>> mapWhere);

    int update(@Param("mapUpdate") Map<String, Object> mapUpdate, @Param("mapWhere") Map<String, List<Map<String, Object>>> mapWhere);

    Page<Map<String, Object>> select(@Param("columns") Set<String> columns, @Param("mapWhere") Map<String, List<Map<String, Object>>> mapWhere);

    int batchInsert(@Param("list") List<Map<String, Object>> list);
}
