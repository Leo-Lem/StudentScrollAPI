package studentscroll.api.security.authz;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.security.JSONWebToken;
import studentscroll.api.security.StudentDetailsService;

public class JWTFilter extends OncePerRequestFilter {

  @Autowired
  private StudentDetailsService service;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      val token = parseJWT(request);

      if (token != null) {
        val email = token.getUsername();
        val details = service.loadUserByUsername(email);

        if (token.validate(details)) {
          val authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (ExpiredJwtException e) {
      Logger.getLogger("JWTFilter").warning("JWT is expired.");
    } catch (Exception e) {
      Logger.getLogger("JWTFilter").warning("Failed to authenticate: " + e.getMessage());
    }

    filterChain.doFilter(request, response);
  }

  private JSONWebToken parseJWT(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth)) {
      String token;
      if (headerAuth.startsWith("Bearer "))
        token = headerAuth.substring(7, headerAuth.length());
      else {
        token = headerAuth;
        Logger.getLogger("JWTFilter").warning("Header is missing Bearer prefix.");
      }

      return new JSONWebToken(token);
    }

    return null;

  }

}
