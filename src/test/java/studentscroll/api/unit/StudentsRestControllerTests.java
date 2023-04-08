package studentscroll.api.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import jakarta.persistence.EntityExistsException;
import lombok.val;
import studentscroll.api.students.data.Profile;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.services.StudentService;
import studentscroll.api.students.web.StudentsRestController;
import studentscroll.api.students.web.dto.*;

public class StudentsRestControllerTests {

  @Mock
  private StudentService service;

  @InjectMocks
  private StudentsRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenEmailIsNotRegistered_whenRegistering_thenReturns200AndAStudentResponse() {
    String name = "Xavier Yoolan", email = "xyz@abc.com", password = "4321";
    val request = new CreateStudentRequest(name, email, password);

    when(service.create(name, email, password))
        .thenReturn(new Student(email, "abc123", new Profile(name)).setId(1L));

    ResponseEntity<?> response = controller.registerUser(request);

    assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(200));
    StudentResponse body = (StudentResponse) response.getBody();
    if (body != null) {
      assertNotNull(body.getId());
      assertEquals(name, body.getName());
      assertEquals(email, body.getEmail());
    }
  }

  @Test
  public void givenEmailIsRegistered_whenRegistering_thenReturns409() {
    String email = "xyz@abc.com";
    val request = new CreateStudentRequest("", email, "");

    when(service.create("", email, ""))
        .thenThrow(new EntityExistsException());

    ResponseEntity<?> response = controller.registerUser(request);

    assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(409));
  }

}
