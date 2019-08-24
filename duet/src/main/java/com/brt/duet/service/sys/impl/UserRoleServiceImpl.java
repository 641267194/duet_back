package com.brt.duet.service.sys.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brt.duet.dao.sys.UserRoleDao;
import com.brt.duet.service.sys.UserRoleService;
import com.github.pagehelper.Page;

/**
 * @author 方杰
 * @date 2019年7月23日
 * @description 用户组表Service接口实现类
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public int insert(Map<String, Object> mapInsert) {
        return userRoleDao.insert(mapInsert);
    }

    @Override
    public int delete(Map<String, List<Map<String, Object>>> mapWhere) {
        return userRoleDao.delete(mapWhere);
    }

    @Override
    public int update(Map<String, Object> mapUpdate, Map<String, List<Map<String, Object>>> mapWhere) {
        return userRoleDao.update(mapUpdate, mapWhere);
    }

    @Override
    public Page<Map<String, Object>> select(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere) {
        return userRoleDao.select(columns, mapWhere);
    }

}
