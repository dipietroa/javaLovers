package io.javalovers.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class TokenRefreshFilter extends OncePerRequestFilter {

  @Autowired
  private TokenUtils tokenUtils;

  private String headerKey;

  public TokenRefreshFilter(String headerKey) {
    this.headerKey = headerKey;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // We refresh the token only if the user is authenticated
    if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      String token = httpRequest.getHeader(headerKey);
      if (token != null) {
        token = tokenUtils.refreshToken(token);
        response.addHeader(headerKey, token);
      }
    }
    filterChain.doFilter(request, response);
  }

}
