package com.wang.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang.health.constant.MessageConstant;
import com.wang.health.constant.RedisMessageConstant;
import com.wang.health.entity.Result;
import com.wang.health.pojo.Order;
import com.wang.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/28', 0028 8:08:05
 * @Email:summer_6121@163.com
 */

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 提交预约
     *
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String, String> orderInfo) {
        //验证校验
        //验证校验前端提交的验证码是否和Redis中的验证码一致
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + orderInfo.get("telephone");
        //先从Redis中拿出来验证码
        Jedis jedis = jedisPool.getResource();
        String codeInRedis = jedis.get(key);
        //Redis中没有值，就提示重新获取验证码
        if (StringUtils.isEmpty(codeInRedis)) {
            return new Result(false, "请重新获取验证码");
        }
        //有值，比较验证码是否一致
        //3. codeInRedis有值, 则比较前端的验证码是否一致
        if (!codeInRedis.equals(orderInfo.get("validateCode"))) {
            //   不一样，则返回验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //   一样，删除redis中的验证码，防止重复提交
        jedis.del(key);
        //   设置预约类型 --微信预约 写死
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        //   继续执行调用服务的方法预约
        Order order = orderService.submit(orderInfo);
        //4. 返回订单信息给页面
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS,order);
    }

    /**
     * 通过订单id找到会员id的信息
     * @param id
     * @return
     */
    @PostMapping("/findById")
    public Result findById(int id){
        Map<String,String> orderInfo = orderService.findOrderDetailById(id);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }
}
