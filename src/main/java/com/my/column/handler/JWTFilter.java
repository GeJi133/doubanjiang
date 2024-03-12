package com.my.column.handler;
 
import com.my.column.config.SecurityConfig;
import com.my.column.service.SecurityService;
import com.my.column.util.JWTUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
 
/**
 * 检验jwt
 * 1、判断请求头是否携带jwt
 *   否：放行不处理
 *   是：走到第二步
 * 2、对前端传过来的jwt解密
 *   否：放行不处理
 *   是：走到第三步
 * 3、获取Redis的jwt
 *   获取不到：放行不处理
 *   获取到：走到第四步
 * 4、对比jwt
 *   否：放行不处理
 *   是：走到第五步
 * 5、给jwt续期
 */
@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private SecurityService securityService;
    /**
     * StringRedisTemplate和RedisTemplate
     */
    @Autowired
    private StringRedisTemplate redisTemplate;
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        /**
         *  * 1、判断请求头是否携带jwt
         *  *   否：放行不处理
         *  *   是：走到第二步
         */
        String jwt=httpServletRequest.getHeader("jwt");
        if(jwt==null){
            filterChain.doFilter(httpServletRequest,httpServletResponse);//交给过滤器处理
            return;
        }
        /**
         *  * 2、对前端传过来的jwt解密
         *  *   否：放行不处理
         *  *   是：走到第三步
         */
        if(!JWTUtil.decode(jwt)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        /**
         *  * 3、获取Redis的jwt
         *  *   获取不到：放行不处理
         *  *   获取到：走到第四步
         */
        Map payLoad=JWTUtil.getPayLoad(jwt);
        String username=(String) payLoad.get("username");
        String redisJwt=redisTemplate.opsForValue().get("jwt:"+payLoad.get("username"));
        if(redisJwt==null){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        /**
         *  * 4、对比jwt
         *  *   否：放行不处理
         *  *   是：走到第五步
         */
        if(!jwt.equals(redisJwt)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        /**
         * * 5、给jwt续期
         */
        redisTemplate.opsForValue().set("jwt:"+payLoad.get("username"),jwt,30, TimeUnit.MINUTES);
 
        //把用户信息放到security容器中
        UserDetails userDetails=securityService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken upa=new UsernamePasswordAuthenticationToken( userDetails.getUsername(),
                                                                                         userDetails.getPassword(),
                                                                                         userDetails.getAuthorities());
        //把信息放到security容器中
        SecurityContextHolder.getContext().setAuthentication(upa);
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}