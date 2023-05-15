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
import studentscroll.api.security.auth.*;
import studentscroll.api.security.authz.*;

@SecurityScheme(name = "token", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@Configuration
public class SecurityConfiguration {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private IsStudentAuthz isStudentAuthz;

  @Autowired
  private IsPosterAuthz isPosterAuthz;

  @Autowired
  private isFollowerAuthz isFollowerAuthz;

  @Autowired
  private isSenderOrReceiverAuthz isSenderOrReceiverAuthz;

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
        .cors().and().csrf().disable()
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.GET, "/docs*/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/students", "/signin").permitAll())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.GET,
                "/students/{studentId}/profile",
                "/posts", "/posts/{postId}",
                "/students/{studentId}/followers",
                "/students/{studentId}/follows")
            .authenticated()
            .requestMatchers(HttpMethod.POST, "/posts", "/chats")
            .authenticated())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.POST, "/students/{studentId}/followers/{followerId}").access(isFollowerAuthz)
            .requestMatchers(HttpMethod.DELETE, "/students/{studentId}/followers/{followerId}").access(isFollowerAuthz)
            .requestMatchers("/chats/{chatId}").access(isSenderOrReceiverAuthz)
            .requestMatchers("/students/{studentId}", "/students/{studentId}/**").access(isStudentAuthz)
            .requestMatchers("/posts/{postId}").access(isPosterAuthz))
        .authorizeHttpRequests(authz -> authz.anyRequest().denyAll())
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

}