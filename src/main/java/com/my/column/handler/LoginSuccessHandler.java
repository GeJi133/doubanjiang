package com.my.column.handler;
 
import com.alibaba.fastjson.JSON;
import com.my.column.entity.HttpResult;
 
import com.my.column.util.JWTUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
 
/**
 * 登录成功处理器
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
 
    @Autowired
    private StringRedisTemplate redisTemplate;
 
    @SneakyThrows//注解抛异常
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication)
            throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        User user=(User)authentication.getPrincipal();
        String username=user.getUsername();
        Map map=new HashMap();
        map.put("username",username);
        String jwt=JWTUtil.createJWT(map);
        //拿jwt干啥：1、放到redis，2、把jwt传到前端
        redisTemplate.opsForValue().set("jwt:"+username,jwt,30, TimeUnit.MINUTES);
        System.out.println("login success handler");
        httpServletResponse.getWriter().write(JSON.toJSONString(new HttpResult().ok(jwt)));
    }
}