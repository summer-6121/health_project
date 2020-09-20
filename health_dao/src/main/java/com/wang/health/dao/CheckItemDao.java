package com.wang.health.dao;

import com.github.pagehelper.Page;
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

    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项的分页查询
     * @return
     */
    Page<CheckItem> findByCondition(String queryString);

    /**
     * 通过id删除检查项（没有和检查组关联的检查项）
     * @param id
     * @return
     */
    void deleteById(Integer id);

    /**
     * 先查询检查项是否存在于检查组里面
     * （检查组是否使用了检查项）
     * @param id
     * @return
     */
    int findCountByCheckItemId(Integer id);

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
