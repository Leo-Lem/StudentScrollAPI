package studentscroll.api.auth.dto;

import lombok.*;

@Data
public class UpdateCredentialsRequest {

  @NonNull
  private final String currentPassword;

  private final String newEmail;

  private final String newPassword;

}