package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.students.StudentsRestController;
import studentscroll.api.students.data.*;
import studentscroll.api.students.services.*;
import studentscroll.api.students.web.dto.*;

public class StudentsRestControllerTests {

  @Mock
  private AuthenticationManager authManager;

  @Mock
  private StudentService studentService;

  @InjectMocks
  private StudentsRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenEmailIsNotRegistered_whenRegistering_thenReturnsStudent() {
    String name = "Xavier Yoolan", email = "xyz@abc.com", password = "4321";
    val request = new CreateStudentRequest(name, email, password);

    when(studentService.create(name, email, password))
        .thenReturn(new Student(email, "abc123", new Profile(name)).setId(1L));

    StudentResponse response = controller.create(request, mock(HttpServletResponse.class));

    assertNotNull(response.getId());
    assertEquals(name, response.getName());
    assertEquals(email, response.getEmail());
  }

  @Test
  public void givenEmailIsRegistered_whenRegistering_thenThrowsEntityExistsException() {
    String email = "xyz@abc.com";
    val request = new CreateStudentRequest("", email, "");

    when(studentService.create("", email, ""))
        .thenThrow(new EntityExistsException());

    assertThrows(EntityExistsException.class, () -> controller.create(request, mock(HttpServletResponse.class)));
  }

  @Test
  public void givenStudentExists_whenReading_thenReturnsStudent() {
    val studentId = 1L;

    when(studentService.read(studentId))
        .thenAnswer(i -> new Student()
            .setId((Long) i.getArguments()[0])
            .setProfile(new Profile("James")));

    assertNotNull(controller.read(studentId));
  }

  @Test
  public void givenStudentExists_whenUpdating_thenReturnsUpdatedStudent() {
    val studentId = 1L;
    String currentPassword = "1234", newEmail = "new@e.mail";
    val request = new UpdateStudentRequest(currentPassword, newEmail, null);

    when(authManager.authenticate(any()))
        .thenReturn(new UsernamePasswordAuthenticationToken(
            newEmail, currentPassword, Set.of(new SimpleGrantedAuthority("User"))));

    when(studentService.read(studentId))
        .thenReturn(new Student().setPassword(currentPassword).setId(studentId).setProfile(new Profile("")));

    when(studentService.update(anyLong(), any(), any()))
        .thenReturn(new Student().setId(studentId).setProfile(new Profile("")));

    assertNotNull(controller.update(studentId, request));
  }

  @Test
  public void givenStudentExists_whenDeleting_thenDoesNotThrow() {
    assertDoesNotThrow(() -> controller.delete(1L));
  }

  @Test
  public void givenStudentDoesNotExist_whenReadingUpdatingDeleting_thenThrowsEntityNotFoundException() {
    val studentId = 1L;

    when(studentService.read(studentId))
        .thenThrow(new EntityNotFoundException());

    when(studentService.update(studentId, Optional.empty(), Optional.empty()))
        .thenThrow(new EntityNotFoundException());

    doThrow(new EntityNotFoundException())
        .when(studentService).delete(studentId);

    assertThrows(EntityNotFoundException.class, () -> controller.read(studentId));
    assertThrows(EntityNotFoundException.class,
        () -> controller.update(studentId, new UpdateStudentRequest("1234", null, null)));
    assertThrows(EntityNotFoundException.class, () -> controller.delete(studentId));
  }

}
