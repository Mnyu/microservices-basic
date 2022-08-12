package com.learn.photoapp.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class MyGlobalPreFilter implements GlobalFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    log.info("******** My first pre-filter is executed... ********");
    String path = exchange.getRequest().getPath().toString();
    log.info("Request path : " + path);
    HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
    log.info("Headers : ");
    httpHeaders.keySet().forEach(headerName -> log.info(headerName + " " + httpHeaders.getFirst(headerName)));
    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
