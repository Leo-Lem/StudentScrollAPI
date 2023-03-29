package studentscroll.api.security.jwt;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import studentscroll.api.students.data.Student;

@Component
@SuppressWarnings("JavaUtilDate")
public class JWTUtils {

  private Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private int jwtExpiration = 86400;

  public String generateJWTToken(Authentication authentication) {

    Student student = (Student) authentication.getPrincipal();

    Date issuance = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(issuance);
    calendar.add(Calendar.SECOND, jwtExpiration);
    Date expiration = calendar.getTime();

    return Jwts.builder()
        .setSubject(student.getUsername())
        .setIssuedAt(issuance)
        .setExpiration(expiration)
        .signWith(jwtKey)
        .compact();
  }

  public String getEmailFromJwtToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(jwtKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateJwtToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(jwtKey)
          .build()
          .parseClaimsJws(token);

      return true;
    } catch (SecurityException e) {
      System.err.println("Invalid JWT signature: " + e.getMessage());
    } catch (MalformedJwtException e) {
      System.err.println("Invalid JWT token:" + e.getMessage());
    } catch (ExpiredJwtException e) {
      System.err.println("JWT token is expired:" + e.getMessage());
    } catch (UnsupportedJwtException e) {
      System.err.println("JWT token is unsupported: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println("JWT claims string is empty: " + e.getMessage());
    }

    return false;
  }

}
