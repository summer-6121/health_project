package com.wang.health.dao;

import com.wang.health.pojo.CheckItem;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/18', 0018 18:47:48
 * @Email:summer_6121@163.com
 */
public interface CheckItemDao {
    /**
     * 查询所有
     * @return
     */
    List<CheckItem> findAll();
}
