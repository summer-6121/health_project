package com.wang.health.service;

import com.wang.exception.HealthException;
import com.wang.health.entity.PageResult;
import com.wang.health.entity.QueryPageBean;
import com.wang.health.pojo.CheckGroup;
import com.wang.health.pojo.CheckItem;

import java.util.List;

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

    /**
     * 通过检查组的id查询数据进行回显
     * @param id
     * @return
     */
    CheckGroup findById(int id);

    /**
     * 通过检查组的id查询  选中  的检查项
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 修改检查组信息以及选中的检查项信息
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 删除检查组
     * @param id
     * @return
     */
    void deleteById(Integer id) throws HealthException;
}
