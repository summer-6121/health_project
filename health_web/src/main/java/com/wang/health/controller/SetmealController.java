package com.wang.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang.health.constant.MessageConstant;
import com.wang.health.entity.PageResult;
import com.wang.health.entity.QueryPageBean;
import com.wang.health.entity.Result;
import com.wang.health.pojo.Setmeal;
import com.wang.health.service.SetmealService;
import com.wang.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/22', 0022 10:45:31
 * @Email:summer_6121@163.com
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    
    private static final Logger log = LoggerFactory.getLogger(SetmealController.class);
    
    //订阅
    @Reference
    private SetmealService setmealService;
    
    @Autowired
    private JedisPool jedisPool;
    
    //步骤：
    //1.获取原有图片的名称，截取得到像.jpg的后缀名
    //2.生成一个唯一的文件名，然后拼接后缀名  ---七牛云上的文件名保证唯一
    //3.调用七牛云来上传文件
    //4.返回数据给页面
    
    @PostMapping("upload")
    public Result upload(MultipartFile imgFile){
        //1.获取原有图片的名称，截取得到像.jpg的后缀名
        String originalFilename = imgFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        //生成一个唯一的文件名，然后拼接后缀名 
        String filename = UUID.randomUUID() + extension;
        //调用七牛云来上传文件
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),filename);
            
            //返回数据给页面
            Map<String,String> map = new HashMap<>();
            map.put("imgName",filename);
            map.put("domain",QiNiuUtils.DOMAIN);
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("上传图片失败",e);
        }
        
        return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PostMapping("add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        Integer setmealId = setmealService.add(setmeal,checkgroupIds);
        // 添加redis生成静态页面的任务
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key, setmealId+"|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 套餐信息分页查询
     * @return
     */
    @PostMapping("findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    /**
     * 通过id查询套餐信息
     * @return
     */
    @GetMapping("findById")
    public Result findById(int id){
        Setmeal setmeal = setmealService.findById(id);
        //前端需要回显图片，要给到全路径
        Map<String,Object> map = new HashMap<>();
        map.put("setmeal",setmeal);
        map.put("domain",QiNiuUtils.DOMAIN);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,map);
    }

    /**
     * 勾选属于这个id的检查组列表的前面的框
     * @return
     */
    @GetMapping("findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(int id){
        List<Integer> checkGroupIds = setmealService.findCheckGroupIdsBySetmealId(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkGroupIds);
    }

    /**
     * 修改套餐信息
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PostMapping("update")
    public Result update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        setmealService.update(setmeal,checkgroupIds);
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key, setmeal.getId()+"|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    /**
     * 删除套餐
     * @param id
     * @return
     */
    @GetMapping("delete")
    public Result delete(int id){
        setmealService.delete(id);
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key, id+"|0|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
