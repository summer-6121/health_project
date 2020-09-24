package com.wang.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang.health.constant.MessageConstant;
import com.wang.health.entity.Result;
import com.wang.health.pojo.OrderSetting;
import com.wang.health.service.OrderSettingService;
import com.wang.utils.POIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/22', 0022 20:10:56
 * @Email:summer_6121@163.com
 */

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSettingController.class);
    
    //订阅
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 上传文件
     * 批量导入预约设置，【注意】参数名必须与前端的参数名name的值要一致，否则得到的是null
     * @param excelFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            //读取Excel内容
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //转成 List<OrderSetting>
            List<OrderSetting> orderSettingList = new ArrayList<>(strings.size());
            //日期格式解析
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            OrderSetting orderSetting = null;
            for (String[] dataArr : strings) {
                //dataArr 代表一行记录 0：表示日期，1：表示数量
                Date orderDate = sdf.parse(dataArr[0]);
                orderSetting = new OrderSetting(orderDate,Integer.valueOf(dataArr[1]));
                orderSettingList.add(orderSetting);
            }
            
            //调用服务
            orderSettingService.add(orderSettingList);
            //返回给页面显示
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
            
        } catch (Exception e) {
            LOGGER.error("批量导入失败",e);
        }
        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }

    /**
     * 通过年份和月份查询预约设置信息
     * @param month
     * @return
     */
    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        List<Map<String,Integer>> monthData = orderSettingService.getOrderSettingByMonth(month);
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,monthData);
    }

    /**
     * 根据日期更新预约设置
     * @param orderSetting
     * @return
     */
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        orderSettingService.editNumberByDate(orderSetting);
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
