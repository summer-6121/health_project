package com.wang.health.service;

import com.wang.health.entity.PageResult;
import com.wang.health.entity.QueryPageBean;
import com.wang.health.pojo.CheckGroup;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/20', 0020 18:29:43
 * @Email:summer_6121@163.com
 */
public interface CheckGroupService {

    /**
     * 检查组的分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);
}
