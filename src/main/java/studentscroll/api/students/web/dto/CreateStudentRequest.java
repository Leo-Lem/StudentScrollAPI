package studentscroll.api.students.web.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
public class CreateStudentRequest {

  @NonNull
  private String name;

  @NonNull
  private String email;

  @NonNull
  private String password;

}