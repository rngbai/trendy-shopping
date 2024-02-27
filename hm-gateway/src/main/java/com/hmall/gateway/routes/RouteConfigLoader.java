package com.hmall.gateway.routes;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import io.github.classgraph.json.JSONUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author lixiaobai
 * @date 2023/11/20
 */
@Component
@RequiredArgsConstructor
public class RouteConfigLoader {
    private final NacosConfigManager nacosConfigManager;
    private final RouteDefinitionWriter writer;
    private static final String DATE_ID="gateway-routes.json";
    private static final String GROUP="DEFAULT_GROUP";
    private Set<String> routeIds=new HashSet<>();

    @PostConstruct //使用bean的生命周期，当前成员变量被初始化加载后就进行bean注入
    public void initRouteConfig() throws NacosException {
        //1.注册监听器并首次拉取配置
        String configInfo = nacosConfigManager.getConfigService()
                .getConfigAndSignListener(DATE_ID, GROUP, 1000, new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return Executors.newSingleThreadExecutor();
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        //监听到路由变更，更新路由表
                        updateRouteConfigInfo(configInfo);
                    }
                });
        //2.写入路由表
        updateRouteConfigInfo(configInfo);
    }

    private void updateRouteConfigInfo(String configInfo) {
        //1.解析路由信息
        List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);
        //2.删除旧的路由
        for (String routeId : routeIds) {
            writer.delete(Mono.just(routeId)).subscribe();
        }
        //3.判断是否有新路由
        if (routeDefinitions==null||routeDefinitions.isEmpty()){
            //无新路由
            return;
        }
        //4.更新路由
        routeIds = new HashSet<>(routeDefinitions.size());
        for (RouteDefinition routeDefinition : routeDefinitions) {
            //4.1更新路由
            writer.save(Mono.just(routeDefinition)).subscribe();
            //4.2记录路由id，方便后面删除
            routeIds.add(routeDefinition.getId());
        }
    }
}
