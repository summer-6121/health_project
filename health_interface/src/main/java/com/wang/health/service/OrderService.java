package com.wang.health.service;

import com.wang.exception.HealthException;
import com.wang.health.pojo.Order;

import java.util.Map;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/28', 0028 8:27:05
 * @Email:summer_6121@163.com
 */
public interface OrderService {
    /**
     * 提交预约
     *
     * @return
     */
    Order submit(Map<String,String> orderInfo) throws HealthException;

    /**
     * 通过订单id找到会员id的信息
     * @param id
     * @return
     */
    Map<String,String> findOrderDetailById(int id);
}
