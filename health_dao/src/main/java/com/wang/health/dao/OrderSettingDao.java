package com.wang.health.dao;

import com.wang.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /**
     * 通过年份和月份查询预约设置信息
     * @param strDate
     * @param endDate
     * @return
     */
    List<Map<String,Integer>> getOrderSettingByMonth(@Param("startDate") String strDate, @Param("endDate") String endDate);

    /**
     * 更新已预约人数
     * @param orderSetting
     * @return
     */
    int editReservationsByOrderDate(OrderSetting orderSetting);
}
