package com.wang.health.controller;

import com.wang.exception.HealthException;
import com.wang.health.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/19', 0019 19:31:03
 * @Email:summer_6121@163.com
 */


/**
 * 全局异常处理
 */
@RestControllerAdvice
public class HealthExceptionAdvice {
    /**
     * info：打印日志，记录流程性的内容
     * debug：记录一些重要的数据  id，orderID，userID
     * error：记录有异常的堆栈信息，代替e.printStarkTrace();
     */
    private static final Logger log = LoggerFactory.getLogger(HealthExceptionAdvice.class);
    
    
    /**
     * 自定义抛出的异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException e){
        log.error("违反业务逻辑",e);
        return new Result(false,e.getMessage());
    }

    /**
     * 所有未知的异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error("发生异常",e);
        return new Result(false, "发生未知错误，操作失败，请联系管理员");
    }

    /**
     * 用户访问拒绝异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e){
        log.error("没有权限",e);
        return new Result(false, "权限不足");
    }
}
