package com.wang.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang.health.service.SetmealService;
import com.wang.utils.QiNiuUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description: 定时清理七牛上的垃圾图片
 * User: Eric
 */
@Component("cleanImgJob")
public class CleanImgJob {

    /**
     * 订阅服务
     */
    @Reference
    private SetmealService setmealService;

    public void cleanImg(){
        // 查出7牛上的s所有图片
        List<String> imgIn7Niu = QiNiuUtils.listFile();
        // 查出数据库中的所有图片
        List<String> imgInDb = setmealService.findImgs();
        // 7牛的-数据库的 imgIn7Niu剩下的就是要删除的
        imgIn7Niu.removeAll(imgInDb);
        // 把剩下的图片名转成数组
        String[] strings = imgIn7Niu.toArray(new String[]{});
        // 删除7牛上的垃圾图片
        QiNiuUtils.removeFiles(strings);
    }
}
