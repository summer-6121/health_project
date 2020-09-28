package com.wang.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang.health.constant.MessageConstant;
import com.wang.health.entity.PageResult;
import com.wang.health.entity.QueryPageBean;
import com.wang.health.entity.Result;
import com.wang.health.pojo.CheckItem;
import com.wang.health.service.CheckItemService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckItem> checkItemList = checkItemService.findAll();
        Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
        return result;
    }

    /**
     * 新增检查项
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem){
        checkItemService.add(checkItem);
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }
    /**
     * 检查项的分页查询
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);
    }

    /**
     * 通过id删除检查项（没有和检查组关联的检查项）
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(Integer id){
        checkItemService.deleteById(id);
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    /**
     * 通过id进行查询
     */
    @GetMapping("/findById")
    public Result findById(Integer id){
        CheckItem checkItem = checkItemService.findById(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }
    /**
     * 更新检查项
     * @param checkItem
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
        // 调用服务更新
        checkItemService.update(checkItem);
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
}
