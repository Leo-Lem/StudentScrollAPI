package studentscroll.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import studentscroll.api.security.authz.IsFollowerAuthz;
import studentscroll.api.security.authz.IsParticipantAuthz;
import studentscroll.api.security.authz.IsPosterAuthz;
import studentscroll.api.security.authz.IsSenderAuthz;
import studentscroll.api.security.authz.IsStudentAuthz;
import studentscroll.api.security.authz.JWTFilter;

@SecurityScheme(name = "token", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@Configuration
public class SecurityConfiguration {

  @Autowired
  private StudentDetailsService userDetailsService;

  @Autowired
  private IsStudentAuthz isStudentAuthz;

  @Autowired
  private IsPosterAuthz isPosterAuthz;

  @Autowired
  private IsFollowerAuthz isFollowerAuthz;

  @Autowired
  private IsParticipantAuthz isParticipantAuthz;

  @Autowired
  private IsSenderAuthz isSenderAuthz;

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
            .requestMatchers(HttpMethod.POST, "/authentication").permitAll())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.PUT, "/authentication").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/authentication").authenticated()
            .requestMatchers("/settings").authenticated())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.GET,
                "/students",
                "/students/{studentId}/profile",
                "/posts", "/posts/{postId}",
                "/students/{studentId}/followers",
                "/students/{studentId}/follows")
            .authenticated()
            .requestMatchers(HttpMethod.POST, "/posts", "/chats")
            .authenticated()
            .requestMatchers(HttpMethod.GET, "chats").authenticated())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.POST, "/students/{studentId}/followers/{followerId}").access(isFollowerAuthz)
            .requestMatchers(HttpMethod.DELETE, "/students/{studentId}/followers/{followerId}").access(isFollowerAuthz))
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/chats/{chatId}").access(isParticipantAuthz)
            .requestMatchers(HttpMethod.POST, "/chats/{chatId}/messages").access(isParticipantAuthz)
            .requestMatchers(HttpMethod.GET, "/chats/{chatId}/messages/{messageId}").access(isParticipantAuthz)
            .requestMatchers("/chats/{chatId}/messages/{messageId}").access(isSenderAuthz))
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/posts/{postId}").access(isPosterAuthz))
        .authorizeHttpRequests(authz -> authz.anyRequest().denyAll())
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

}