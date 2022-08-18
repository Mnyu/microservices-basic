package com.learn.photoapp.users;

import com.learn.photoapp.users.feign.FeignErrorDecoder;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class PhotoAppUsersServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PhotoAppUsersServiceApplication.class, args);
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  // Enable HTTP Requests Logging in Feign Client
  @Bean
  Logger.Level feignLogger() {
    return Logger.Level.FULL;
  }

  // Handle Response Errors with Feign Error Decoder
  @Bean
  FeignErrorDecoder getFeignErrorDecoder() {
    return new FeignErrorDecoder();
  }
}
