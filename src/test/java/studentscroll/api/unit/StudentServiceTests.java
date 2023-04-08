package studentscroll.api.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityExistsException;
import lombok.val;
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

}
