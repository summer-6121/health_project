package com.wang.health.dao;

import com.wang.health.pojo.OrderSetting;

import java.util.Date;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/22', 0022 20:59:00
 * @Email:summer_6121@163.com
 */
public interface OrderSettingDao {
    /**
     * 通过日期查询预约设置信息
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 通过日期更新最大数量
     * @param orderSetting
     */
    void updateNumber(OrderSetting orderSetting);

    /**
     * 插入预约设置
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);
}
