package studentscroll.api.security.auth;

import lombok.*;

@Data
public class SigninRequest {

  @NonNull
  private String email;

  @NonNull
  private String password;

}
