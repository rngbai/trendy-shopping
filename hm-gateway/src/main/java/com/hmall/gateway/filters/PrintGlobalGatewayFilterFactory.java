package com.hmall.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lixiaobai
 * @date 2023/11/17
 */
@Component
public class PrintGlobalGatewayFilterFactory implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("Global过滤器被执行了");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
