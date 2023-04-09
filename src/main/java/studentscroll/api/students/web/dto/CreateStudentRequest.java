package studentscroll.api.students.web.dto;

import lombok.*;

@Data
public class CreateStudentRequest {

  @NonNull
  private final String name;

  @NonNull
  private final String email;

  @NonNull
  private final String password;

}
