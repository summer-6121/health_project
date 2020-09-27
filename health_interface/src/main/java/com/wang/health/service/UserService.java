package com.wang.health.service;

import com.wang.health.pojo.User;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/26', 0026 20:58:59
 * @Email:summer_6121@163.com
 */
public interface UserService {

    /**
     * 根据用户名字查询用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);
}
