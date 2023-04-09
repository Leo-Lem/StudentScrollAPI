package studentscroll.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.swagger.v3.oas.annotations.enums.*;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import studentscroll.api.security.auth.UserDetailsServiceImpl;
import studentscroll.api.security.authz.IsPosterAuthorizationManager;
import studentscroll.api.security.authz.IsStudentAuthorizationManager;
import studentscroll.api.security.authz.JWTFilter;

@SecurityScheme(name = "token", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@SecurityScheme(name = "student themself", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@Configuration
public class SecurityConfiguration {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private IsStudentAuthorizationManager isStudentAuthz;

  @Autowired
  private IsPosterAuthorizationManager isPosterAuthz;

  @Bean
  public JWTFilter jwtFilter() {
    return new JWTFilter();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
    return security
        .csrf().disable()
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.GET, "/docs*/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/students", "/signin").permitAll())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.GET, "/students/{studentId}/profile", "/posts/{postId}").authenticated()
            .requestMatchers(HttpMethod.POST, "/posts").authenticated())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.PUT, "/students/{studentId}/**").access(isStudentAuthz)
            .requestMatchers("/posts/{postId}").access(isPosterAuthz))
        .authorizeHttpRequests(authz -> authz.anyRequest().denyAll())
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

}