package studentscroll.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import studentscroll.api.security.auth.UserDetailsServiceImpl;
import studentscroll.api.security.authz.JWTFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

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
        // .cors().and().csrf().disable()
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.POST, "/signin").permitAll()
            .anyRequest().denyAll())
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

}