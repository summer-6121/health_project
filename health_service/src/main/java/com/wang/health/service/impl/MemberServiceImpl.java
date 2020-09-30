package com.wang.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wang.health.dao.MemberDao;
import com.wang.health.pojo.Member;
import com.wang.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
     *
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 注册会员
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 统计过去1年每个月的会员总数量
     * 根据月份来统计每个月的会员总数
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        //首先判断months是否为空
        if (months != null && months.size() > 0) {
            //遍历月份
            for (String month : months) {
                //拼接最后一天
                String endDate = month + "-31";
                Integer count = memberDao.findMemberCountBeforeDate(endDate);
                memberCount.add(count);
            }
        }
        return memberCount;
    }
}
