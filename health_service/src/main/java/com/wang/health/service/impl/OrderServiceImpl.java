package com.wang.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wang.exception.HealthException;
import com.wang.health.constant.MessageConstant;
import com.wang.health.dao.MemberDao;
import com.wang.health.dao.OrderDao;
import com.wang.health.dao.OrderSettingDao;
import com.wang.health.pojo.Member;
import com.wang.health.pojo.Order;
import com.wang.health.pojo.OrderSetting;
import com.wang.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/28', 0028 8:28:01
 * @Email:summer_6121@163.com
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 提交预约信息
     *
     * @return
     */
    @Override
    @Transactional
    public Order submit(Map<String, String> orderInfo) throws HealthException {
        //通过日期查询预约设置信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //先从前端拿到预约日期 
        Date orderDate = null;
        try {
            orderDate = sdf.parse(orderInfo.get("orderDate"));
        } catch (ParseException e) {
            throw new HealthException("日期格式不正确，请选择正确日期！");
        }
        //通过前端传过来的日期查询当前日期的预约信息
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        //判断当前日期是否可以预约，如果不存在就报错
        if (orderSetting == null) {
            throw new HealthException("所选日期不能预约，请选择其他日期");
        }
        //存在 ：判断预约已满，满了就报错
        if (orderSetting.getReservations() >= orderSetting.getNumber()){
            throw new HealthException("所选日期预约已满，请选择其他日期");
        }
        
        //判断是否重复预约
        String telephone = orderInfo.get("telephone");
        //通过手机号查询会员信息
        Member member = memberDao.findByTelephone(telephone);
        //会员存在才需要判断是否重复预约
        Order order = new Order();
        order.setOrderDate(orderDate);
        order.setSetmealId(Integer.valueOf(orderInfo.get("setmealId")));
        if (member != null) {
            //查询
            order.setMemberId(member.getId());
            //判断是否重复预约
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0){
                throw new HealthException("改套餐已经预约过了，请不要重复预约");
            }
        }else {
            //会员不存在 就自动注册为会员
            member = new Member();
            // name 从前端来
            member.setName(orderInfo.get("name"));
            // sex 从前端来
            member.setSex(orderInfo.get("sex"));
            // idCard 从前端来
            member.setIdCard(orderInfo.get("idCard"));
            // phoneNumber 从前端来
            member.setPhoneNumber(telephone);
            // regTime 系统时间
            member.setRegTime(new Date());
            // password 可以不填 可以生成
            member.setPassword("123456789");
            // remark 自动注册
            member.setRemark("由预约而注册的");
            // 添加会员
            memberDao.add(member);
            order.setMemberId(member.getId());
        }
        //可预约
        //预约类型
        order.setOrderType(orderInfo.get("orderType"));
        //预约状态
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        //添加t_order 预约信息
        orderDao.add(order);
        //更新预约人数，更新成功就返回，数据没有变更则返回0
        int affectedCount = orderSettingDao.editReservationsByOrderDate(orderSetting);
        if (affectedCount == 0){
            throw new HealthException(MessageConstant.ORDER_FULL);
        }
        //返回新添加的订单对象
        return order;
    }

    /**
     * 通过订单id找到会员id的信息
     * @param id
     * @return
     */
    @Override
    public Map<String, String> findOrderDetailById(int id) {
        return orderDao.findById4Detail(id);
    }
}
