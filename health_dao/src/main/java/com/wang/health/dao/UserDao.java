package com.wang.health.dao;

import com.wang.health.pojo.User;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/27', 0027 16:59:08
 * @Email:summer_6121@163.com
 */
public interface UserDao {

    /**
     * 根据用户名字查询用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);
}
