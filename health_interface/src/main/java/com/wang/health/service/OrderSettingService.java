package com.wang.health.service;

import com.wang.exception.HealthException;
import com.wang.health.pojo.OrderSetting;

import java.util.List;

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
}
