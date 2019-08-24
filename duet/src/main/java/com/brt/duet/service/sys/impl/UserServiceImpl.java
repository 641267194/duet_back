package com.brt.duet.service.sys.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brt.duet.dao.sys.UserDao;
import com.brt.duet.dao.sys.UserRoleDao;
import com.brt.duet.service.sys.UserService;
import com.github.pagehelper.Page;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 用户表Service接口实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public int insert(Map<String, Object> mapInsert) {
        return userDao.insert(mapInsert);
    }

    @Override
    public int delete(Map<String, List<Map<String, Object>>> mapWhere) {
        return userDao.delete(mapWhere);
    }

    @Override
    public int update(Map<String, Object> mapUpdate, Map<String, List<Map<String, Object>>> mapWhere) {
        return userDao.update(mapUpdate, mapWhere);
    }

    @Override
    public Page<Map<String, Object>> select(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere) {
        return userDao.select(columns, mapWhere);
    }

	@Override
	@Transactional
	public int insert(Map<String, Object> user, List<Map<String, Object>> userRoles) {
		int re = userDao.insert(user);
		if (re > 0) {
			if (userRoles != null && userRoles.size() > 0) {
				userRoleDao.batchInsert(userRoles);
			}
		}
		return re;
	}

	@Override
	@Transactional
	public int update(Map<String, Object> user, Map<String, List<Map<String, Object>>> mapWhere,
			List<Map<String, Object>> userRoles, Map<String, List<Map<String, Object>>> userRoleMapWhere) {
		int re = userDao.update(user, mapWhere);
		if (re > 0) {
			userRoleDao.delete(userRoleMapWhere);
			if (userRoles != null && userRoles.size() > 0) {
				userRoleDao.batchInsert(userRoles);
			}
		}
		return re;
	}

	@Override
	public Page<Map<String, Object>> selectWithRole(Set<String> columns,
			Map<String, List<Map<String, Object>>> mapWhere) {
		return userDao.selectWithRole(columns, mapWhere);
	}

}
