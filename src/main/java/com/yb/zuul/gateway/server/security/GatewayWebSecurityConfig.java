package com.yb.zuul.gateway.server.security;

import org.apache.http.HttpStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Description: 网关的安全控制类(必须的)
 * author biaoyang
 * date 2019/4/8 000810:47
 */
@Configuration
@EnableWebSecurity
public class GatewayWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //这里仅仅只是很简略的配置了一下,其中/login和/index没有,并且放开actuator的监控的资源
        http.httpBasic().disable().csrf().disable();
        http.authorizeRequests().antMatchers("/actuator/**").permitAll()
                //记得这里需要对其他的路径开启安全认证
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login").successForwardUrl("/index").and()
                .logout().logoutSuccessUrl("/login")
                //设置需要登录的返回提示信息(必要)
                .and().exceptionHandling().authenticationEntryPoint((request,response,exception)->{
                    response.setHeader("Content-type", "text/html;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                    //这里可以封装一个类toJSONString,就是对象字符串化
                    response.getWriter().write("请登录");
        });
    }
}
