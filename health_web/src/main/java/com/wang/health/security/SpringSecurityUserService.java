package com.wang.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wang.health.pojo.Permission;
import com.wang.health.pojo.Role;
import com.wang.health.pojo.User;
import com.wang.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author:WangLiPeng
 * @Date:2020/9/26', 0026 20:55:09
 * @Email:summer_6121@163.com
 */

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //数据库中的用户信息
        //根据用户名字查询用户信息
        User user = userService.findByUsername(username);
        if (user != null) {
            //用户存在
            //用户的权限集合
            List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
            GrantedAuthority authority =null;
            //用户拥有的角色
            Set<Role> roles = user.getRoles();
            if (roles != null) {
                for (Role role : roles) {
                    //授予用户角色  注意：这里是角色的关键字
                    authority = new SimpleGrantedAuthority(role.getKeyword());
                    authorityList.add(authority);
                    //角色下的权限集合
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions != null) {
                        //授予权限
                        for (Permission permission : permissions) {
                            //注意：这里也是关键字
                            authority = new SimpleGrantedAuthority(permission.getKeyword());
                            authorityList.add(authority);
                        }
                    }
                }
            }
            //认证用户的信息
            org.springframework.security.core.userdetails.User user1 = 
                    new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorityList);
            return user1;

        }
        //否则查不到就报错
        return null;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("admin"));
    }
}
