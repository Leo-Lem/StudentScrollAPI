package studentscroll.api.account.web.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
public class AuthenticationRequest {

  private final String name;

  @NonNull
  private final String email;

  @NonNull
  private final String password;

}
