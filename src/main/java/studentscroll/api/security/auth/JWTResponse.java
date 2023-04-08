package studentscroll.api.security.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JWTResponse {

  private Long id;
  private String email;
  private String token;
  private final String type = "Bearer";

}
