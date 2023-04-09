package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.data.StudentRepository;
import studentscroll.api.students.services.StudentService;

public class StudentServiceTests {

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private StudentRepository repo;

  @InjectMocks
  private StudentService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenEmailIsNotRegistered_whenCreatingNewStudent_thenCreatesStudentAndReturnsNewStudentAndPasswordIsEncrypted() {
    String name = "Abraham Corvas", email = "abc@xyz.com", password = "1234";

    when(passwordEncoder.encode(password))
        .thenReturn("xyz123");

    when(repo.existsByEmail(email))
        .thenReturn(false);

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    val student = service.create(name, email, password);

    assertEquals(name, student.getProfile().getName());
    assertEquals(email, student.getEmail());
    assertNotEquals(password, student.getPassword());
  }

  @Test
  public void givenEmailIsRegistered_whenRegisteringAgain_thenThrowsEntityExistsException() {
    String name = "Abraham Corvas", email = "abc@xyz.com", password = "1234";

    when(repo.existsByEmail(email))
        .thenReturn(true);

    assertThrows(EntityExistsException.class, () -> service.create(name, email, password));
  }

  @Test
  public void givenStudentExists_whenReadingStudent_thenReturnsStudent() {
    val studentId = 1L;

    when(repo.findById(studentId))
        .thenReturn(Optional.of(new Student()));

    assertDoesNotThrow(() -> assertNotNull(service.read(studentId)));
  }

  @Test
  public void givenStudentExists_whenUpdatingStudent_thenNewDetailsMatch() {
    val studentId = 1L;
    String newEmail = "johnny@gmx.com", newPassword = "my-pony";

    when(repo.findById(studentId))
        .thenReturn(Optional.of(new Student().setId(studentId)));

    when(passwordEncoder.encode(newPassword))
        .thenReturn("xyz123");

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Student student = service.update(studentId, Optional.of(newEmail), Optional.of(newPassword));

    assertEquals(studentId, student.getId());
    assertEquals(newEmail, student.getEmail());
    assertNotEquals(newPassword, student.getPassword());
  }

  @Test
  public void givenStudentExists_whenDeleting_thenDoesNotThrow() {
    val studentId = 1L;

    when(repo.existsById(studentId))
        .thenReturn(true);

    assertDoesNotThrow(() -> service.delete(studentId));
  }

  @Test
  public void givenNoStudentExists_whenReadingUpdatingDeletingStudent_thenThrowsEntityNotFoundException() {
    val studentId = 1L;

    when(repo.existsById(studentId))
        .thenReturn(false);

    when(repo.findById(studentId))
        .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.read(studentId));
    assertThrows(EntityNotFoundException.class, () -> service.update(studentId, Optional.empty(), Optional.empty()));
    assertThrows(EntityNotFoundException.class, () -> service.delete(studentId));
  }

}
