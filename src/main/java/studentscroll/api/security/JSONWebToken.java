package studentscroll.api.security;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@SuppressWarnings("JavaUtilDate")
@AllArgsConstructor
public class JSONWebToken {

  public static final long EXPIRES_AFTER_SECONDS = 24 * 60 * 60;
  private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  private String token;

  public static JSONWebToken generateFrom(UserDetails details) {
    return new JSONWebToken(
        Jwts.builder()
            .setClaims(new HashMap<String, Object>())
            .setSubject(details.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRES_AFTER_SECONDS * 1000))
            .signWith(KEY)
            .compact());
  }

  public Boolean validate(UserDetails details) {
    return (getUsername().equals(details.getUsername())) && !isExpired();
  }

  @Override
  public String toString() {
    return token;
  }

  public String getUsername() {
    return getClaim(Claims::getSubject);
  }

  public Date getExpirationDate() {
    return getClaim(Claims::getExpiration);
  }

  public Boolean isExpired() {
    return getExpirationDate().before(new Date());
  }

  private <T> T getClaim(Function<Claims, T> resolve) {
    return resolve.apply(getAllClaims());
  }

  private Claims getAllClaims() {
    return Jwts.parserBuilder()
        .setSigningKey(KEY).build()
        .parseClaimsJws(token).getBody();
  }

}
