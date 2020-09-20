package com.wang.health.dao;

import com.github.pagehelper.Page;
import com.wang.health.pojo.CheckGroup;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/20', 0020 18:37:03
 * @Email:summer_6121@163.com
 */
public interface CheckGroupDao {

    /**
     * 检查组的分页查询
     * @param queryString
     * @return
     */
    Page<CheckGroup> findPage(String queryString);
    

    /**
     * 添加检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 添加检查组和检查项的关系   事务控制
     * @param checkGroupId
     * @param checkitemId
     * @return
     */

    void addCheckGroupCheckItem(Integer checkGroupId, Integer checkitemId);
}
