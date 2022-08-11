package com.learn.photoapp.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.photoapp.users.model.LoginTO;
import com.learn.photoapp.users.model.UserTO;
import com.learn.photoapp.users.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final String TOKEN_EXPIRATION_TIME = "token.expiration";
  private static final String TOKEN_SECRET = "token.secret";
  private static final String TOKEN = "token";
  private static final String USER_ID = "userId";

  private Environment env;
  private UserService userService;

  public AuthenticationFilter(Environment env, UserService userService,
      AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager); // This needs to be set as attemptAuthentication() is using getAuthenticationManager()
    this.userService = userService;
    this.env = env;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                HttpServletResponse response) throws AuthenticationException {
    try {
      LoginTO creds = new ObjectMapper().readValue(request.getInputStream(), LoginTO.class);
      return getAuthenticationManager()
          .authenticate(new UsernamePasswordAuthenticationToken(creds.getEmail(),
              creds.getPassword(), new ArrayList<>()));
    } catch (IOException e) {
       throw new RuntimeException(e);
    }
  }

  // Point D :
  // Once Spring Framework is done performing user authentication it will call successfulAuthentication().
  // The job of this method will be to :
  // 1) Take UserDetails and then use to UserDetails to generate a JWT Token
  // 2) Add this JWT token to the HTTP Response header and return it back with HTTP Response.
  //
  // The client application will then be able to read this JWT token from the Response header and
  // use this JWT token in subsequent requests to our app as authorization header.
  //
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain, Authentication authResult) throws IOException, ServletException {
    String userName = ((User) authResult.getPrincipal()).getUsername();
    UserTO userTO = userService.getUserByEmail(userName);
    String token = Jwts.builder()
        .setSubject(userTO.getUserId())
        .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty(TOKEN_EXPIRATION_TIME))))
        .signWith(SignatureAlgorithm.HS256, env.getProperty(TOKEN_SECRET))
        .compact();
    response.addHeader(TOKEN, token);
    response.addHeader(USER_ID, userTO.getUserId());
    // Add public user id of User to header as well so that client application can take both token
    // and user id and use this to validate subsequent HTTP requests.
  }
}
