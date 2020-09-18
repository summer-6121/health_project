package com.wang.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wang.health.dao.CheckItemDao;
import com.wang.health.pojo.CheckItem;
import com.wang.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/18', 0018 18:41:36
 * @Email:summer_6121@163.com
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {
    
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
