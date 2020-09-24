package com.wang.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang.health.constant.MessageConstant;
import com.wang.health.entity.Result;
import com.wang.health.pojo.Setmeal;
import com.wang.health.service.SetmealService;
import com.wang.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/24', 0024 15:21:04
 * @Email:summer_6121@163.com
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    
    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有套餐
     * @return
     */
    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        //查询所有套餐
        List<Setmeal> list = setmealService.findAll();
        //拼接全路径
        list.forEach(s ->{
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });
        return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
    }

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @GetMapping("findDetailById")
    public Result findDetailById(int id){
        //查询
        Setmeal setmeal = setmealService.findDetailById(id);
        //设置图片完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
