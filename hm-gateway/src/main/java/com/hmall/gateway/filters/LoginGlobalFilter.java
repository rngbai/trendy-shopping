package com.hmall.gateway.filters;

import cn.hutool.core.text.AntPathMatcher;
import com.hmall.common.exception.UnauthorizedException;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.config.JwtProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.file.PathMatcher;
import java.util.List;

/**
 * @author lixiaobai
 * @date 2023/11/18
 */
@Component
@AllArgsConstructor
public class LoginGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    private final JwtTool jwtTool;

    private final AntPathMatcher pathMatcher=new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request
        ServerHttpRequest request = exchange.getRequest();
        //2.判断当前请求是否需要拦截
        if(isExclude(request)){
            //无需拦截直接放行
            return chain.filter(exchange);
        }
        //3.获取token
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");
        if (headers != null){
            token=headers.get(0);
        }
        //4.解析token
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
            System.out.println("UserId="+userId);
        } catch (Exception e) {
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(401);
            //不放行
            return response.setComplete();
        }
        //5.传递用户信息到下游服务
        String userInfo = userId.toString();
        ServerWebExchange ex = exchange.mutate()
                .request(builder -> builder.header("user-info", userInfo))
                .build();
        //放行
        System.out.println(userId);
        return chain.filter(ex);
    }

    private boolean isExclude(ServerHttpRequest request) {
        boolean flag = false;
        //1.获取当前路径
        String path = request.getPath().toString();
        //2.要放行的路径
        for (String excludePath : authProperties.getExcludePaths()) {
            boolean match = pathMatcher.match(excludePath, path);
            if (match){
                flag=true;
                break;
            }
        }
        return flag;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
