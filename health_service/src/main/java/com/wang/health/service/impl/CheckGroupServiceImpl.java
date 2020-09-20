package com.wang.health.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.health.dao.CheckGroupDao;
import com.wang.health.entity.PageResult;
import com.wang.health.entity.QueryPageBean;
import com.wang.health.pojo.CheckGroup;
import com.wang.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}
