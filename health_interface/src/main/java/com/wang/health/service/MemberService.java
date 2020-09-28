package com.wang.health.service;

import com.wang.health.pojo.Member;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/28', 0028 20:26:45
 * @Email:summer_6121@163.com
 */
public interface MemberService {
    /**
     * 通过手机号码判断是否为会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 注册会员
     * @param member
     */
    void add(Member member);
}
