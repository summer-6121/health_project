package com.wang.health.dao;

import com.github.pagehelper.Page;
import com.wang.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/22', 0022 17:01:27
 * @Email:summer_6121@163.com
 */
public interface SetmealDao {

    //添加套餐
    void add(Setmeal setmeal);

    //添加套餐和检查组的关系
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    //根据查询条件查询套餐信息
    Page<Setmeal> findByCondition(String queryString);

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
     * 添加套餐信息
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 删除套餐和检查组之间的旧关系
     * @param setmealId
     */
    void deleteSetmealCheckGroup(Integer setmealId);

    /**
     * 查询套餐是否被订单使用
     * @param id
     * @return
     */
    int findOrderCountBySetmealId(int id);

    /**
     * 根据id删除套餐
     * @param id
     */
    void deleteById(int id);
}
