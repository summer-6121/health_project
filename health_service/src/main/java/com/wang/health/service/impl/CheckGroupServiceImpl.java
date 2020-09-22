package com.wang.health.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.exception.HealthException;
import com.wang.health.dao.CheckGroupDao;
import com.wang.health.entity.PageResult;
import com.wang.health.entity.QueryPageBean;
import com.wang.health.pojo.CheckGroup;
import com.wang.health.pojo.CheckItem;
import com.wang.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/20', 0020 18:34:02
 * @Email:summer_6121@163.com
 */

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 检查组的分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        
        //使用PageHelper  得到当前页码和大小
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //有条件就模糊查询
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
            //拼接  %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        
        //条件查询
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());
        PageResult<CheckGroup> pageResult = new PageResult<CheckGroup>(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 添加检查组   事务控制
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //添加检查组
        checkGroupDao.add(checkGroup);
        //获取检查组的id
        Integer checkGroupId = checkGroup.getId();
        //循环遍历选中的检查项id
        if (checkitemIds != null){
            for (Integer checkitemId : checkitemIds) {
                //添加检查组和检查项的关系
                checkGroupDao.addCheckGroupCheckItem(checkGroupId,checkitemId);
            }
        }
    }

    /**
     * 通过检查组的id查询数据进行回显
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(int id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 通过检查组的id查询  选中  的检查项
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 修改检查组信息以及选中的检查项信息
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //先更新检查组
        checkGroupDao.update(checkGroup);
        //删除旧关系(检查项和检查组的关系)
        checkGroupDao.deleteCheckGroupCheckItem(checkGroup.getId());
        //建立新关系
        if (null != checkitemIds){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroup.getId(),checkitemId);
            }
        }
        
    }

    /**
     * 删除检查组
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void deleteById(Integer id) throws HealthException {
        //先检查id对应的检查组是否被套餐使用
        int rows = checkGroupDao.findSetmealCountByCheckGroupId(id);
        if (rows > 0){
            //被使用了
            throw new HealthException("此检查组被套餐使用，无法删除！");
        }
        //没有被套餐使用，可以删除
        
        //先删除检查组和检查项的关系
        checkGroupDao.deleteCheckGroupCheckItem(id);
        //删除检查组
        checkGroupDao.deleteById(id);
    }

    /**
     * 获取所有检查组列表数据，绑定到tableData
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
