package com.yb.zuul.gateway.server.security;

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
        http.authorizeRequests().antMatchers("/actuator/**").permitAll().and()
                .formLogin().loginPage("/login").successForwardUrl("/index").and()
                .logout().logoutSuccessUrl("/login");
    }
}
