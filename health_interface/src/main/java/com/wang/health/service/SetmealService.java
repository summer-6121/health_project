package com.wang.health.service;

import com.wang.exception.HealthException;
import com.wang.health.entity.PageResult;
import com.wang.health.entity.QueryPageBean;
import com.wang.health.pojo.Setmeal;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/22', 0022 16:57:13
 * @Email:summer_6121@163.com
 */
public interface SetmealService {

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds) throws HealthException;

    /**
     * 套餐信息分页查询
     * @return
     */
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    /**
     * 通过id查询套餐信息
     * @return
     */
    Setmeal findById(int id);

    /**
     * 勾选属于这个id的检查组列表的前面的框
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(int id);

    /**
     * 修改套餐信息
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 删除套餐
     * @param id
     * @return
     */
    void delete(int id) throws HealthException;
}
