package com.my.column.config;
 
import com.my.column.handler.*;
import com.my.column.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
 
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
 
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true,prePostEnabled = true) //作用：自动开启注解式授权
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JWTFilter jWTFilter;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private SecurityService securityService;
 
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService);
    }
 
    /**
     * 自定义登录页面
     * @param http
     * @throws Exception
     */
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin() //告诉框架自定义页面
               .loginPage("/login") //登录页面地址
               .loginProcessingUrl("/tologin") //对应表单提交的action
                .usernameParameter("loginName") // 自定义username属性名，默认username
                .passwordParameter("password") // 自定义password属性名，默认password
               .successHandler(loginSuccessHandler)
//                .defaultSuccessUrl("/")//设置默认登录成功跳转页面
               .failureHandler(new LoginFailHandler())
               .permitAll();//对login.html和dologin请求放行
        http.exceptionHandling()
                .accessDeniedHandler(new NoAuthHandler())
                .authenticationEntryPoint(new NoLoginHandler());
        http.authorizeRequests()
//                .antMatchers("/hello").hasAuthority("anth_techer") //有anth_techer的权限才能访问/hello
//                .antMatchers("/hello").hasAnyAuthority("anth_techer","hello") //有anth_techer或hello的权限才能访问/hello
//                .antMatchers("/hello").hasRole("anth_techer") //会在anth_techer加ROLE_前缀
                .antMatchers("/index").permitAll() //配置免登陆接口
                .antMatchers("/login").permitAll() //配置免登陆接口
                .antMatchers("/index").permitAll() //配置免登陆接口
                .antMatchers("/js/**","/layui/**","/css/**","/plugins/**","/images/*","/fonts/**","/**/*.png","/**/*.jpg").permitAll()
                .anyRequest().authenticated(); //所有请求都拦截

        /**
         * 把jwtfilter注入进来
         */
        http.addFilterAfter(jWTFilter, UsernamePasswordAuthenticationFilter.class);
        /**
         * 把session禁掉
         */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
 
        //防跨站脚本攻击关闭
         http.csrf().disable();
         //运行跨域
         http.cors();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/global/**");
    }

    /**
     * 数据加密类
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}