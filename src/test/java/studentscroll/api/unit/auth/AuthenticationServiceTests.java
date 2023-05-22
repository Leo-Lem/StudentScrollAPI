package studentscroll.api.unit.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityExistsException;
import lombok.val;
import studentscroll.api.auth.AuthenticationService;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.data.StudentRepository;
import studentscroll.api.utils.TestUtils;

public class AuthenticationServiceTests {

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private StudentRepository repo;

  @InjectMocks
  private AuthenticationService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    TestUtils.unauthenticate();
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
  public void givenEmailIsUsed_whenRegisteringAgain_thenThrowsEntityExistsException() {
    String name = "Abraham Corvas", email = "abc@xyz.com", password = "1234";

    when(repo.existsByEmail(email))
        .thenReturn(true);

    assertThrows(EntityExistsException.class, () -> service.create(name, email, password));
  }

  @Test
  public void givenIsAuthenticated_whenUpdating_thenNewDetailsMatch() {
    val student = new Student().setId(1L);

    TestUtils.authenticate(student);

    String newEmail = "johnny@gmx.com", newPassword = "my-pony";

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    when(passwordEncoder.encode(newPassword))
        .thenReturn("xyz123");

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Student newStudent = service.update(Optional.of(newEmail), Optional.of(newPassword));

    assertEquals(student.getId(), newStudent.getId());
    assertEquals(newEmail, newStudent.getEmail());
    assertNotEquals(newPassword, newStudent.getPassword());
  }

  @Test
  public void givenIsAuthenticated_whenDeleting_thenDoesNotThrow() {
    val student = new Student().setId(1L);

    TestUtils.authenticate(student);

    assertDoesNotThrow(() -> service.delete());
  }

  @Test
  public void givenIsNotAuthenticated_whenReadingCurrent_thenThrowsIllegalStateException() {
    TestUtils.authenticate(null);
    assertThrows(IllegalStateException.class, () -> service.readCurrent());
  }

}
