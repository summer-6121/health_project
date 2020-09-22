package com.wang.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wang.exception.HealthException;
import com.wang.health.dao.OrderSettingDao;
import com.wang.health.pojo.OrderSetting;
import com.wang.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/22', 0022 20:47:22
 * @Email:summer_6121@163.com
 */
//发布
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    
    /**
     * 批量导入预约设置
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) throws HealthException {
        //遍历
        if (orderSettingList != null && orderSettingList.size() > 0){
            for (OrderSetting orderSetting : orderSettingList) {
                //通过日期查询预约设置信息
                OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
                //存在
                if (osInDB != null) {
                    //判断要设置的最大预约数量是否小于已经预约的数量
                    if (orderSetting.getNumber() < osInDB.getReservations()){
                        //是  就抛出异常
                        throw new HealthException("最大预约数量不能小于已经预约数量");
                    }
                    //否 通过日期更新最大预约数量
                    orderSettingDao.updateNumber(orderSetting);
                }else {
                    //不存在，插入预约设置
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }
}
