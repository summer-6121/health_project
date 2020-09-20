package com.wang.health.service;

import com.wang.exception.HealthException;
import com.wang.health.entity.PageResult;
import com.wang.health.entity.QueryPageBean;
import com.wang.health.pojo.CheckItem;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/18', 0018 18:38:36
 * @Email:summer_6121@163.com
 */
public interface CheckItemService {
    /**
     * 查询所有
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项的分页查询
     * @return
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    /**
     * 通过id删除检查项（没有和检查组关联的检查项）
     * dubbo 如果不声明，会使用RuntimeException包装自定义的异常
     * @param id
     * @return
     */
    void deleteById(Integer id) throws HealthException;

    /**
     * 通过id进行查询
     */
    CheckItem findById(Integer id);

    /**
     * 更新检查项
     * @param checkItem
     * @return
     */
    void update(CheckItem checkItem);
}
