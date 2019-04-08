package com.yb.zuul.gateway.server.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * -------------------网关这里做的过滤并不能很好的处理访问的开放的问题,例如登录页面的放开,这里就没法弄,
 * -------------------网关必须要使用web
 * Description:自定义过滤器,仅仅只需继承抽象类ZuulFilter,实现/重写其方法即可
 * author biaoyang
 * date 2019/4/3 000319:52
 */
@Component//把过滤器实例化到spring容器里,也可以通过@Bean注解的方法实例化
public class AccessFilter extends ZuulFilter {


    /**
     * Zuul有一下四种过滤器
     * "pre":是在请求路由到具体的服务之前执行,这种类型的过滤器可以做安全校验,例如身份校验,参数校验等
     * "routing":它用于将请求路由到具体的微服务实例,在默认情况下,它使用Http Client进行网络请求
     * "post":它是在请求已被路由到微服务后执行,一般情况下,用作收集统计信息,指标,以及将响应传输到客户端
     * "error":它是在其他过滤器发生错误时执行
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤顺序,值越小,越早执行该过滤器
     * 指定该Filter执行的顺序（Filter从小到大执行）
     * DEBUG_FILTER_ORDER = 1;
     * FORM_BODY_WRAPPER_FILTER_ORDER = -1;
     * PRE_DECORATION_FILTER_ORDER = 5;
     * RIBBON_ROUTING_FILTER_ORDER = 10;
     * SEND_ERROR_FILTER_ORDER = 0;
     * SEND_FORWARD_FILTER_ORDER = 500;
     * SEND_RESPONSE_FILTER_ORDER = 1000;
     * SIMPLE_HOST_ROUTING_FILTER_ORDER = 100;
     * SERVLET_30_WRAPPER_FILTER_ORDER = -2;
     * SERVLET_DETECTION_FILTER_ORDER = -3;
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 表示该过滤器是否过滤逻辑,如果是ture,则执行run()方法;如果是false,则不执行run()方法
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //可以在这里添加逻辑代码,一般来说都没有必要
        return true;
    }

    /**
     * 这里做安全校验,身份校验,一般做token合法性校验,获取登录用户信息,存入redis
     * (存储jwt的jti到redis,可以让其随时失效)或者存入InheritableThreadLocal
     * InheritableThreadLocal当前线程创建子线程时,子线程能够继承父线程中的ThreadLocal变量;
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //获取请求对象
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        //获取请求头里的token信息-->这里默认只认key为Authorization的头,jwt都是用这个
        String token = request.getHeader("Authorization");
        //为了测试方便,这里先假装有合法的token
        token="Bearer I am legal token";
        //判断token的合法性
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            //通过Jwt工具验证签名
            //通过则解析荷载,把用户信息存入到对应的地方
            //生成jti绑定jwt并存入redis
        } else {
            //这里根之前使用的那个AuthorizationEntryPoint的实现类返回的信息差不多的
            ctx.setSendZuulResponse(false);
            //设置响应码
            ctx.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);//401
            //让浏览器用utf8来解析返回的数据,需要和下面的setCharacterEncoding的编码保持一致
            ctx.addZuulResponseHeader("Content-type", "text/html;charset=UTF-8");
            //告诉servlet用UTF-8转码，而不是用默认的ISO8859-1,这个需要在写中文前设置
            ctx.getResponse().setCharacterEncoding("UTF-8");
            //可以直接返回提示请登录,也可以字符串化一个对象返回
            ctx.setResponseBody("请登录");
            //还可以使用下面这种方式,只是下面的需要处理异常,不推荐
            //ctx.getResponse().getWriter().write("请登录");
        }
        return null;
    }
}
