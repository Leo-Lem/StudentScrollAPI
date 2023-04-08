package studentscroll.api.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import studentscroll.api.security.auth.UserDetailsServiceImpl;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.data.StudentRepository;

public class UserDetailsServiceImplTests {

  @Mock
  private StudentRepository repo;

  @InjectMocks
  private UserDetailsServiceImpl service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenUserExists_whenLoadingByEmail_thenDoesNotThrow() {
    String existingEmail = "abc@xyz.com";

    when(repo.findByEmail(existingEmail))
        .thenReturn(Optional.of(new Student()));

    assertDoesNotThrow(() -> service.loadUserByUsername(existingEmail));
  }

  @Test
  public void givenUserDoesNotExist_whenLoadingByEmail_thenThrowsUserNotFound() {
    String nonExistingEmail = "abc@xyz.com";

    when(repo.findByEmail(nonExistingEmail))
        .thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(nonExistingEmail));
  }

}
