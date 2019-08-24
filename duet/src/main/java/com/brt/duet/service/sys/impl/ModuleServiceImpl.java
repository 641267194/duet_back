package com.brt.duet.service.sys.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brt.duet.dao.sys.ModuleDao;
import com.brt.duet.service.sys.ModuleService;
import com.github.pagehelper.Page;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 模块表Service接口实现类
 */
@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public int insert(Map<String, Object> mapInsert) {
        return moduleDao.insert(mapInsert);
    }

    @Override
    public int delete(Map<String, List<Map<String, Object>>> mapWhere) {
        return moduleDao.delete(mapWhere);
    }

    @Override
    public int update(Map<String, Object> mapUpdate, Map<String, List<Map<String, Object>>> mapWhere) {
        return moduleDao.update(mapUpdate, mapWhere);
    }

    @Override
    public Page<Map<String, Object>> select(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere) {
        return moduleDao.select(columns, mapWhere);
    }

	@Override
	public List<Map<String, Object>> getModulesByUserId(String userId) {
		return moduleDao.getModulesByUserId(userId);
	}

}
