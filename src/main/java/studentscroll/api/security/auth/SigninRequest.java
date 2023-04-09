package studentscroll.api.security.auth;

import lombok.*;

@Data
public class SigninRequest {

  @NonNull
  private final String email;

  @NonNull
  private final String password;

}
