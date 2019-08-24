package com.brt.duet.service.sys.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brt.duet.dao.sys.RoleDao;
import com.brt.duet.dao.sys.RoleModuleDao;
import com.brt.duet.service.sys.RoleService;
import com.github.pagehelper.Page;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 用户组表Service接口实现类
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    
    @Autowired
    private RoleModuleDao roleModuleDao;

    @Override
    public int insert(Map<String, Object> mapInsert) {
        return roleDao.insert(mapInsert);
    }

    @Override
    public int delete(Map<String, List<Map<String, Object>>> mapWhere) {
        return roleDao.delete(mapWhere);
    }

    @Override
    public int update(Map<String, Object> mapUpdate, Map<String, List<Map<String, Object>>> mapWhere) {
        return roleDao.update(mapUpdate, mapWhere);
    }

    @Override
    public Page<Map<String, Object>> select(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere) {
        return roleDao.select(columns, mapWhere);
    }

	@Override
	@Transactional
	public int insert(Map<String, Object> role, List<Map<String, Object>> roleModules) {
		int re = roleDao.insert(role);
		if (re > 0) {
			if (roleModules != null && roleModules.size() > 0) {
				roleModuleDao.batchInsert(roleModules);
			}
		}
		return re;
	}
	
	@Override
	@Transactional
	public int delete(Map<String, List<Map<String, Object>>> roleMapWhere,
			Map<String, List<Map<String, Object>>> roleModuleMapWhere) {
		roleModuleDao.delete(roleModuleMapWhere);
		return roleDao.delete(roleMapWhere);
	}

	@Override
	@Transactional
	public int update(Map<String, Object> role, Map<String, List<Map<String, Object>>> mapWhere,
			List<Map<String, Object>> roleModules, Map<String, List<Map<String, Object>>> roleModuleMapWhere) {
		int re = roleDao.update(role, mapWhere);
		if (re > 0) {
			roleModuleDao.delete(roleModuleMapWhere);
			if (roleModules != null && roleModules.size() > 0) {
				roleModuleDao.batchInsert(roleModules);
			}
		}
		return re;
	}

}
