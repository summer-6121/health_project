package com.wang.exception;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/19', 0019 19:27:23
 * @Email:summer_6121@163.com
 */

/**
 * 自定义异常
 */
public class HealthException extends RuntimeException{
    public HealthException(String message){
        super(message);
    }
}
