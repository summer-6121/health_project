package com.wang.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.wang.health.constant.MessageConstant;
import com.wang.health.constant.RedisMessageConstant;
import com.wang.health.entity.Result;
import com.wang.utils.SMSUtils;
import com.wang.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/26', 0026 8:04:05
 * @Email:summer_6121@163.com
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {


    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/send4Order")
    public Result send4Order(String telephone) {
        //生成验证码之前检查一下是否发送过了，通过Redis获取key为手机号码，看是否存在
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        //Redis中的验证码
        //1. 先从redis中取出验证码codeInRedis key=手机号码
        String codeInRedis = jedis.get(key);
        //  验证前端提交过来的验证码与redis的验证码是否一致
        if (codeInRedis == null) {
            //2. codeInRedis没值，提示用户重新获取验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            try {
                //发送验证码
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
                //存入Redis,设置有效时间为10分钟
                jedis.setex(key, 10 * 60, code + "");
                //返回成功
                return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                //e.printStackTrace();
                //发送失败
                return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        //说明存在
        return new Result(false,MessageConstant.SEND_VALIDATECODE);
        //3. codeInRedis有值, 则比较前端的验证码是否一致
        //   不一样，则返回验证码错误
        //   一样，删除redis中的验证码，防止重复提交
        //   继续执行调用服务的方法预约
        //   设置预约类型
        //4. 返回订单信息给页面
    }
}
