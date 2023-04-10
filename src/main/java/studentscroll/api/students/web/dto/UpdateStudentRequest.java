package studentscroll.api.students.web.dto;

import lombok.*;

@Data
public class UpdateStudentRequest {

  @NonNull
  private final String currentPassword;

  private final String newEmail;

  private final String newPassword;

}