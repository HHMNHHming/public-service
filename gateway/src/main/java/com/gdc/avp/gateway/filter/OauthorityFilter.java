package com.gdc.avp.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;

@Component
public class OauthorityFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        // Allow 允许放行，不进行验证；
        if(headers.containsKey("Allow")){
            System.out.println("请求放行···");
            return chain.filter(exchange);
        }

        //无通行证继续判断
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        return chain.filter(exchange);
    }
    @Override
    public int getOrder() {
        return 4;
    }
}
