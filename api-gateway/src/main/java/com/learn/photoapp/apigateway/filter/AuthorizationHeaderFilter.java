package com.learn.photoapp.apigateway.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// For custom filters Spring Framework provides a mechanism (Config class below) to pass
// configuration properties to our custom filter and then, we can use those configuration
// properties to configure the behaviour of our filter.
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

  private static final String BEARER = "Bearer";
  private static final String TOKEN_SECRET = "token.secret";

  private Environment env;

  public static class Config {
    // Put configuration properties here
  }

  @Autowired
  public AuthorizationHeaderFilter(Environment env) {
    super(Config.class);
    this.env = env;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest serverHttpRequest = exchange.getRequest();
      if (!serverHttpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        return onError(exchange, "No Authorization Header", HttpStatus.UNAUTHORIZED);
      }
      String authValue = serverHttpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
      String jwt = authValue.replace(BEARER, "").trim();
      if (!isJwtValid(jwt)) {
        return onError(exchange, "JWT is not valid.",HttpStatus.UNAUTHORIZED);
      }
      return chain.filter(exchange);
    };
  }

  private Mono<Void> onError(ServerWebExchange serverWebExchange, String err, HttpStatus httpStatus) {
    ServerHttpResponse serverHttpResponse = serverWebExchange.getResponse();
    serverHttpResponse.setStatusCode(httpStatus);
    return serverHttpResponse.setComplete();
  }

  private boolean isJwtValid(String jwt) {
    try {
      String subject = Jwts.parser()
          .setSigningKey(env.getProperty(TOKEN_SECRET))
          .parseClaimsJws(jwt)
          .getBody()
          .getSubject();
      return subject != null && !subject.isEmpty();
    } catch (Exception e) {
      return false;
    }
  }
}
