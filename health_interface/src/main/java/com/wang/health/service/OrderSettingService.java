package com.wang.health.service;

import com.wang.exception.HealthException;
import com.wang.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/22', 0022 20:42:10
 * @Email:summer_6121@163.com
 */
public interface OrderSettingService {

    /**
     * 批量导入预约设置
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList)throws HealthException;

    /**
     * 通过年份和月份查询预约设置信息
     * @param month
     * @return
     */
    List<Map<String,Integer>> getOrderSettingByMonth(String month);

    /**
     * 根据日期更新预约设置
     * @param orderSetting
     * @return
     */
    void editNumberByDate(OrderSetting orderSetting) throws HealthException;
}
