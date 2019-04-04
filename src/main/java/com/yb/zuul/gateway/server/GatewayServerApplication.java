package com.yb.zuul.gateway.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

//我们常用的就是@EnableZuulProxy,过多的过滤器也用不上
//@EnableZuulServer//开启路由(代理),高配版,拥有更多的过滤器,性能较@EnableZuulProxy低
@EnableZuulProxy//开启路由(代理),低配版,拥有更少的过滤器,性能较@EnableZuulServer高
@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

}
