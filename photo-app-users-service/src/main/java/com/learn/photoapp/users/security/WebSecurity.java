package com.learn.photoapp.users.security;

import com.learn.photoapp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

  private static final String GATEWAY_IP = "gateway.ip";
  private static final String LOGIN_PATH= "login.path";

  private Environment env;
  private UserService userService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.env = env;
    this.userService = userService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable(); // because we will be using JWT tokens.
    httpSecurity.headers().frameOptions().disable(); // for h2-console to open in browser.
    //http.authorizeRequests().antMatchers("/users/**").permitAll();

    httpSecurity.authorizeRequests()
      .antMatchers("/**")
      .hasIpAddress(env.getProperty(GATEWAY_IP)) // Only allow requests from API GATEWAY IP Address.
      .and()
      .addFilter(getAuthenticationFilter()) // Register custom Authentication Filter with HTTP Security.
    ;
  }

  private AuthenticationFilter getAuthenticationFilter() throws Exception {
    // AuthenticationManager needs to be set as attemptAuthentication() is using getAuthenticationManager()
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(env, userService,
        authenticationManager());

    // with this -> the new login url will be : http://localhost:8082/users-ws/users/login
    authenticationFilter.setFilterProcessesUrl(env.getProperty(LOGIN_PATH));

    return authenticationFilter;
  }

  // Point A :
  // Till now, we have implemented a custom filter class(AuthenticationFilter.class) and registered this
  // custom filter class with HTTP security. So now, when user sends and HTTP request to perform a login,
  // our custom filter will be triggered and the method attemptAuthentication() will be called.
  //
  // Spring Framework will attempt to authenticate the user but to do that, it needs to know where
  // and how to find the user details. And to help Spring Framework find the user details for
  // authentication we will implement methods -
  // 1) configure(AuthenticationManagerBuilder) and
  // 2) successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication)
  //
  // configure(AuthenticationManagerBuilder) : configure the AuthenticationManagerBuilder and let
  // Spring Framework know which service will be used to load user details and which password
  // encoder is going to be used.
  //
  // successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication) :
  // Once Spring Framework is done performing user authentication it will call successfulAuthentication()
  @Override
  protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
  }
}
