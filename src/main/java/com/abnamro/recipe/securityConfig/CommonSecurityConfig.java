package com.abnamro.recipe.securityConfig;

import com.abnamro.recipe.filter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class CommonSecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .cors()
        .disable()
        .csrf(
            (csrf) ->
                csrf.csrfTokenRequestHandler(requestHandler)
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
        .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
        .authorizeHttpRequests()
        .requestMatchers("/api/v1/ingredient/**")
        .hasRole("ADMIN")
        .requestMatchers("/api/v1/recipe")
        .hasRole("ADMIN")
        .requestMatchers("/api/v1/recipe?id=")
        .hasRole("ADMIN")
        .requestMatchers("/api/v1/recipe/search/**")
        .hasAnyRole("ADMIN", "CUSTOMER")
        .requestMatchers("/register")
        .permitAll()
        .requestMatchers("/user")
        .authenticated()
        .and()
        .authorizeHttpRequests()
        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
        .permitAll()
        .and()
        .csrf()
        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
        .and()
        .headers(headers -> headers.frameOptions().sameOrigin())
        .formLogin()
        .and()
        .httpBasic();
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
  }
}
