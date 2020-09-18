package com.wang.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang.health.constant.MessageConstant;
import com.wang.health.entity.Result;
import com.wang.health.pojo.CheckItem;
import com.wang.health.service.CheckItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/18', 0018 18:33:31
 * @Email:summer_6121@163.com
 */

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    
    @Reference
    private CheckItemService checkItemService;
    
    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckItem> checkItemList = checkItemService.findAll();
        Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
        return result;
    }
}
