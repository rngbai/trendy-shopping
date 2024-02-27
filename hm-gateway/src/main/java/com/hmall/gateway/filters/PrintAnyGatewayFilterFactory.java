package com.hmall.gateway.filters;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class PrintAnyGatewayFilterFactory extends AbstractGatewayFilterFactory<PrintAnyGatewayFilterFactory.Config> {
    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter(new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                //获取Config的值
                String a = config.getA();
                String b = config.getB();
                String c = config.getC();
                //编写过滤器的逻辑
                System.out.println("c = " + c);
                System.out.println("b = " + b);
                System.out.println("a = " + a);
                return chain.filter(exchange);
            }
        },0);
    }
    @Data
    public static class Config{
        private String a;
        private String b;
        private String c;
    }
    //将变量名词依次返回，顺序很重要，将来读取参数时需要按照顺序来读取

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("a","b","c");
    }
    //返回当前配置类的类型，也就是内部的Config

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }
}