package com.learn.photoapp.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

  private static final String GATEWAY_IP = "gateway.ip";
  private Environment env;

  @Autowired
  public WebSecurity(Environment env) {
    this.env = env;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable(); // because we will be using JWT tokens.
    http.headers().frameOptions().disable(); // for h2-console to open in browser.
    //http.authorizeRequests().antMatchers("/users/**").permitAll();

    // Only allow requests from API GATEWAY IP Address.
    http.authorizeRequests().antMatchers("/**").hasIpAddress(env.getProperty(GATEWAY_IP));

  }
}
