package com.wang.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wang.health.dao.UserDao;
import com.wang.health.pojo.User;
import com.wang.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/27', 0027 16:56:15
 * @Email:summer_6121@163.com
 */

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    
    
    @Autowired
    private UserDao userDao;
    

    /**
     * 根据用户名字查询用户信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
