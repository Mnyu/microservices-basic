package com.learn.photoapp.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class GlobalFiltersConfiguration {

  @Order(1)
  @Bean
  public GlobalFilter secondPreFilter() {
    return ((exchange, chain) -> {
      log.info("******** My second pre-filter is executed... ********");
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        log.info("******** My second post-filter is executed... ********");
      }));
    });
  }

  @Order(2)
  @Bean
  public GlobalFilter thirdPreFilter() {
    return ((exchange, chain) -> {
      log.info("******** My third pre-filter is executed... ********");
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        log.info("******** My third post-filter is executed... ********");
      }));
    });
  }

  @Order(3)
  @Bean
  public GlobalFilter fourthPreFilter() {
    return ((exchange, chain) -> {
      log.info("******** My fourth pre-filter is executed... ********");
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        log.info("******** My fourth post-filter is executed... ********");
      }));
    });
  }

}
