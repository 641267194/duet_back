package com.brt.duet.service.sys.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import com.brt.duet.dao.sys.LogDao;
import com.brt.duet.service.sys.LogService;

/**
 * @author 方杰
 * @date 2019年9月16日
 * @description 系统日志表Service接口实现类
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logDao;

    @Override
    public int insert(Map<String, Object> mapInsert) {
        return logDao.insert(mapInsert);
    }

    @Override
    public int delete(Map<String, List<Map<String, Object>>> mapWhere) {
        return logDao.delete(mapWhere);
    }

    @Override
    public int update(Map<String, Object> mapUpdate, Map<String, List<Map<String, Object>>> mapWhere) {
        return logDao.update(mapUpdate, mapWhere);
    }

    @Override
    public Page<Map<String, Object>> select(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere) {
        return logDao.select(columns, mapWhere);
    }

}
