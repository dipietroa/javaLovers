package io.javalovers.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import io.javalovers.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  @Autowired
  private TokenUtils tokenUtils;
  
  private String headerKey;
  
  public TokenAuthenticationFilter(String headerKey) {
    this.headerKey = headerKey;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String token = httpRequest.getHeader(headerKey);
    String username = tokenUtils.getUsernameFromToken(token);
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      if (tokenUtils.validateToken(token, username)) {
        UserEntity user = new UserEntity(username, "");
        user.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(user);
      }
    }
    chain.doFilter(request, response);
  }
}
