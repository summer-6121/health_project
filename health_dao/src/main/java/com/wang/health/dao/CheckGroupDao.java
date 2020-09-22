package com.wang.health.dao;

import com.github.pagehelper.Page;
import com.wang.health.pojo.CheckGroup;
import com.wang.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

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
     * 更新检查组的信息（修改）
     * @param checkGroup
     */
    void update(CheckGroup checkGroup);

    /**
     * 重新建立检查组和检查项之间的关系
     * @param id
     */
    void deleteCheckGroupCheckItem(Integer id);

    /**
     * 检查id对应的检查组是否被套餐使用
     * @param id
     * @return
     */
    int findSetmealCountByCheckGroupId(Integer id);

    /**
     * 删除检查组
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 获取所有检查组列表数据，绑定到tableData
     * @return
     */
    List<CheckGroup> findAll();
}
