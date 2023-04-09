package studentscroll.api.security.auth;

import lombok.Data;

@Data
public class JWTResponse {

  private final Long id;
  private final String email;
  private final String token;
  private final String type = "Bearer";

}
