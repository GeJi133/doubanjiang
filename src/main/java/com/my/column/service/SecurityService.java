package com.my.column.service;
 

import com.my.column.dao.UserEntityMapper;
import com.my.column.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 
@Service
public class SecurityService implements UserDetailsService {
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserEntityMapper userMapper;
 
    /**
     * username：页面传过来的username
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername"+username);
        UserEntity users=userMapper.selectByLoginName(username);
        if(users!=null) {
            System.out.println("user string:"+users.toString());
            String anths=String.join(",",users.getAuths());
            //username 数据库产查用户信息
//            user.setAnths(AuthorityUtils.commaSeparatedStringToAuthorityList(anths));
            return new User(users.getLoginName(), new BCryptPasswordEncoder().encode(users.getPasswordMd5()),
                    AuthorityUtils.commaSeparatedStringToAuthorityList(anths));
        }
        else {
            throw new UsernameNotFoundException("账号或密码错误");
        }
    }
}