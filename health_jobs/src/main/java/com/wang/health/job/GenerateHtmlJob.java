package com.wang.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang.health.pojo.Setmeal;
import com.wang.health.service.SetmealService;
import com.wang.utils.QiNiuUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/25', 0025 18:18:40
 * @Email:summer_6121@163.com
 */
@Component
public class GenerateHtmlJob {
    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(GenerateHtmlJob.class);

    /**
     * spring创建对象后，调用的初始化方法
     */
    @PostConstruct
    private void init(){
        // 设置模板所在
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(),"/ftl"));
        // 指定默认编码
        configuration.setDefaultEncoding("utf-8");
    }

    /** jedis连接池 */
    @Autowired
    private JedisPool jedisPool;

    /** 订阅套餐服务 */
    @Reference
    private SetmealService setmealService;

    /** 注入freemarker主配置类 */
    @Autowired
    private Configuration configuration;

    /** 生成静态页面存放的目录 */
    @Value("${out_put_path}")
    private String out_put_path;

    /**
     * 任务执行的方法
     */
    @Scheduled(initialDelay = 3000,fixedDelay = 1800000)
    public void doGenerateHtml(){
        // 获取redis连接对象
        Jedis jedis = jedisPool.getResource();
        // redis中的key
        String key = "setmeal:static:html";
        // 获取集合中所有的套餐id
        Set<String> setmealIds = jedis.smembers(key);
        if(null != setmealIds && setmealIds.size() > 0){
            // 有数据则需要处理
            for (String setmealId : setmealIds) {
                //双斜杠表示 转义
                //对value值按 | 切割
                String[] setmealInfo = setmealId.split("\\|");
                // 获取套餐的id
                int id = Integer.valueOf(setmealInfo[0]);
                // 获取操作类型(操作符)
                String operation = setmealInfo[1];
                // 需要生成套餐详情页面的操作
                if("1".equals(operation)) {
                    // 查询套餐详情
                    Setmeal setmealDetail = setmealService.findDetailById(id);
                    // 设置图片的全路径
                    setmealDetail.setImg(QiNiuUtils.DOMAIN + setmealDetail.getImg());
                    // 生成套餐详情静态页面
                    
                    //创建数据模型
                    Map<String,Object> dataMap = new HashMap<>();
                    dataMap.put("setmeal",setmealDetail);
                    //获取模板对象
                    try {
                        //Template template = configuration.getTemplate("mobile_setmeal_detail.ftl");
                        //输出目录
                        String filename = String.format("%s/setmeal_%d.html",out_put_path,setmealDetail.getId());
                        //生成页面
                        generateHtml("mobile_setmeal_detail.ftl",filename,dataMap);
                        //删除对应套餐的id
                        jedis.srem(key,setmealId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    // 删除套餐静态页面
                    //removeSetmealDetailFile(id);
                    String filename = String.format("%s/setmeal_%d.html",out_put_path,id);
                    new File(filename).delete();
                    //删除对应套餐的id
                    jedis.srem(key,setmealId);
                }
            }
            // 套餐列表的数据也发生了变化，要重新生成静态页面
            generateSetmealList();
        }
    }

    /**
     * 生成套餐列表页面
     */
    private void generateSetmealList(){
        // 获取所有套餐信息
        List<Setmeal> setmealList = setmealService.findAll();
        setmealList.forEach(setmeal -> {
            // 设置每个套餐图片的全路径
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        });
        //获取模板文件名
        String templateName  = "mobile_setmeal.ftl";
        // 生成的文件全路径
        String filename = out_put_path + "/mobile_setmeal.html";
        // 构建数据模型
        Map<String,Object> dataMap = new HashMap<String,Object>();
        // key setmealList 与模板中的变量要一致
        dataMap.put("setmealList",setmealList);
        
        
        // 生成页面
        generateHtml(templateName, filename, dataMap);
    }
    /**
     * 生成页面
     * @param templateName
     * @param filename
     * @param dataMap
     */
    private void generateHtml(String templateName,String filename,Map<String,Object> dataMap){
        try {
            Template template = configuration.getTemplate(templateName);

            //创建writer
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
            //填充模板
            template.process(dataMap,writer);
            //关闭writer
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

}
