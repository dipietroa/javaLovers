package io.javalovers.configuration;

import java.util.Arrays;

import io.javalovers.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public TokenUtils tokenUtils() {
    return new TokenUtils("SECRET", 3600l);
  }

  @Bean
  public TokenAuthenticationEntryPoint restAuthenticationEntryPoint() {
    return new TokenAuthenticationEntryPoint();
  }

  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
    TokenAuthenticationFilter filter = new TokenAuthenticationFilter("Authorization");
    filter.setAuthenticationManager(authenticationManagerBean());
    return filter;
  }

  @Bean
  public TokenRefreshFilter tokenRefreshFilter() {
    TokenRefreshFilter filter = new TokenRefreshFilter("Authorization");
    return filter;
  }

  @Bean
  public LocalAuthenticationProvider ldapAuthenticationProvider() {
    return new LocalAuthenticationProvider();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(ldapAuthenticationProvider());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
        "/swagger-ui.html", "/webjars/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and()
      .csrf().disable()
      .exceptionHandling()
        .authenticationEntryPoint(restAuthenticationEntryPoint()).and()
        .authorizeRequests()
          .antMatchers(HttpMethod.DELETE, "/comments/**").authenticated()
          .anyRequest().permitAll().and()
        .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(tokenRefreshFilter(), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(ImmutableList.of("*"));
    configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
    configuration.setExposedHeaders(Arrays.asList("Authorization"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
