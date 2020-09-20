package com.wang.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wang.exception.HealthException;
import com.wang.health.constant.MessageConstant;
import com.wang.health.dao.CheckItemDao;
import com.wang.health.entity.PageResult;
import com.wang.health.entity.QueryPageBean;
import com.wang.health.pojo.CheckItem;
import com.wang.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/18', 0018 18:41:36
 * @Email:summer_6121@163.com
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {
    
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 新增检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 检查项的分页查询
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //使用了PageHelper分页插件  startPage(当前页码，大小--> 每页显示的记录数)
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //判断是否有查询条件，有则要拼接 %
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
            //模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        
        //紧接着查询的语句会被分页，
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        //防止数据丢失  page用的是基础数据类型，没有实现序列化
        PageResult<CheckItem> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        
        return pageResult;
    }

    /**
     * 通过id删除检查项（没有和检查组关联的检查项）
     * @param id
     * @return
     */
    @Override
    public void deleteById(Integer id) throws HealthException {
        //先判断检查项是否被检查组使用了
        //使用dao查询检查项的id是否在t_checkgroup_checkitem表中存在记录
        int row = checkItemDao.findCountByCheckItemId(id);
        //被使用了就不能删除
        if (row > 0){
            //说明被使用了，就不能删除
            //如果进行删除的话会报错
            throw new HealthException("检查项存在于检查组里面，不能进行删除，请联系管理员进行删除！");
        }
        
        //没有被使用就可以进行删除
        checkItemDao.deleteById(id);
    }

    /**
     * 通过id进行查询
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 更新检查项
     * @param checkItem
     * @return
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
}
