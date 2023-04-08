package studentscroll.api.students.web.requestDTO;

import lombok.Data;

@Data
public class CreateStudentRequest {

  private String name;
  private String email;
  private String password;

}
