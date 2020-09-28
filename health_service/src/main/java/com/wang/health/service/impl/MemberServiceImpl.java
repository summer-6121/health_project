package com.wang.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wang.health.dao.MemberDao;
import com.wang.health.pojo.Member;
import com.wang.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/28', 0028 20:28:35
 * @Email:summer_6121@163.com
 */

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    
    @Autowired
    private MemberDao memberDao;
    /**
     * 通过手机号码判断是否为会员
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 注册会员
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }
}
