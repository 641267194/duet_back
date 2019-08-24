package com.brt.duet.service.sys.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brt.duet.dao.sys.RoleModuleDao;
import com.brt.duet.service.sys.RoleModuleService;
import com.github.pagehelper.Page;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 角色模块关联表Service接口实现类
 */
@Service
public class RoleModuleServiceImpl implements RoleModuleService {

    @Autowired
    private RoleModuleDao roleModuleDao;

    @Override
    public int insert(Map<String, Object> mapInsert) {
        return roleModuleDao.insert(mapInsert);
    }

    @Override
    public int delete(Map<String, List<Map<String, Object>>> mapWhere) {
        return roleModuleDao.delete(mapWhere);
    }

    @Override
    public int update(Map<String, Object> mapUpdate, Map<String, List<Map<String, Object>>> mapWhere) {
        return roleModuleDao.update(mapUpdate, mapWhere);
    }

    @Override
    public Page<Map<String, Object>> select(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere) {
        return roleModuleDao.select(columns, mapWhere);
    }

}
