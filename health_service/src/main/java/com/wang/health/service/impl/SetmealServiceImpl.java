package com.wang.health.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.exception.HealthException;
import com.wang.health.dao.SetmealDao;
import com.wang.health.entity.PageResult;
import com.wang.health.entity.QueryPageBean;
import com.wang.health.pojo.Setmeal;
import com.wang.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/22', 0022 16:59:05
 * @Email:summer_6121@163.com
 */

//发布
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 新增套餐
     *
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @Override
    @Transactional
    public Integer add(Setmeal setmeal, Integer[] checkgroupIds)throws HealthException {
        //添加套餐信息
        setmealDao.add(setmeal);
        //获取套餐的id
        Integer setmealId = setmeal.getId();
        //添加套餐和检查组的关系
        if (checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }
        return setmealId;
    }

    /**
     * 套餐信息分页查询
     *
     * @return
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        //先得到当前页码和大小
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //查询条件不为空就拼接
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //拼接  ---- 模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //查询出来的数据会被分页
        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());
        PageResult<Setmeal> pageResult = new PageResult<Setmeal>(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 通过id查询套餐信息
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 勾选属于这个id的检查组列表的前面的框
     * @return
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(int id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐信息
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @Override
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐信息
        setmealDao.update(setmeal);
        //获取套餐的id
        Integer setmealId = setmeal.getId();
        //先删除套餐和检查组的旧关系
        setmealDao.deleteSetmealCheckGroup(setmealId);
        //再添加套餐和检查组的新关系
        if (checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }
    }

    /**
     * 删除套餐
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void delete(int id)throws HealthException {
        // 查询是否被订单使用
        int count = setmealDao.findOrderCountBySetmealId(id);
        if(count > 0){
            // 已经有订单使用了这个套餐，不能删除
            throw new HealthException("已经有订单使用了这个套餐，不能删除！");
        }
        // 先删除套餐与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);
        // 再删除套餐
        setmealDao.deleteById(id);
    }

    /**
     * 查出数据库中的所有图片
     * @return
     */
    @Override
    public List<String> findImgs() {
        return setmealDao.findImgs();
    }
    
    /**
     * 查询所有套餐
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }


}
