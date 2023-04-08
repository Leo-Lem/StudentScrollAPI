package studentscroll.api.students.web.responseDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import studentscroll.api.students.data.Student;

@Data
@RequiredArgsConstructor
public class StudentResponse {

  private final Long id;
  private final String name;
  private final String email;

  public StudentResponse(Student student) {
    this(student.getId(), student.getProfile().getName(), student.getEmail());
  }

}
