package studentscroll.api.unit.account;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.account.services.StudentService;
import studentscroll.api.account.web.AccountRestController;
import studentscroll.api.account.web.dto.AccountResponse;
import studentscroll.api.account.web.dto.AuthenticationRequest;
import studentscroll.api.account.web.dto.UpdateCredentialsRequest;
import studentscroll.api.security.StudentDetailsService;
import studentscroll.api.students.data.Profile;
import studentscroll.api.utils.TestUtils;

public class AccountRestControllerTests {

  @Mock
  private AuthenticationManager authManager;

  @Mock
  private StudentService service;

  @Mock
  private StudentDetailsService detailsService;

  @InjectMocks
  private AccountRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenCrendentialsAreValid_whenRequestIsReceived_thenReturns200AndSomeJWT() {
    String email = "abc@xyz.com", password = "1234";
    val details = new Student(email, password, new Profile(""));
    val request = new AuthenticationRequest(null, email, password);

    when(authManager.authenticate(any(Authentication.class)))
        .thenAnswer(i -> {
          val auth = (Authentication) i.getArguments()[0];
          return new UsernamePasswordAuthenticationToken(
              auth.getPrincipal(), auth.getCredentials(), Set.of(new SimpleGrantedAuthority("USER")));
        });

    when(detailsService.loadUserByUsername(email))
        .thenReturn(details);

    assertTrue(StringUtils.hasText(controller.authenticate(request, mock(HttpServletResponse.class)).toString()));
  }

  @Test
  public void givenCredentialsAreInvalid_whenRequestIsReceived_thenThrowsBadCredentialsException() {
    when(authManager.authenticate(any(Authentication.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    assertThrows(BadCredentialsException.class, () -> controller.authenticate(
      new AuthenticationRequest(null, "abc@xyz.com", "1234"),
      mock(HttpServletResponse.class)));
  }

  @Test
  public void givenEmailIsNotRegistered_whenRegistering_thenReturnsStudent() {
    String name = "Xavier Yoolan", email = "xyz@abc.com", password = "4321";
    val request = new AuthenticationRequest(name, email, password);

    when(service.create(name, email, password))
        .thenReturn(new Student(email, "abc123", new Profile(name)).setId(1L));

    AccountResponse response = controller.authenticate(request, mock(HttpServletResponse.class));

    assertNotNull(response.getId());
    assertEquals(name, response.getName());
    assertEquals(email, response.getEmail());
  }

  @Test
  public void givenEmailIsRegistered_whenRegistering_thenThrowsEntityExistsException() {
    String email = "xyz@abc.com";
    val request = new AuthenticationRequest("", email, "");

    when(service.create("", email, ""))
        .thenThrow(new EntityExistsException());

    assertThrows(EntityExistsException.class, () -> controller.authenticate(request, mock(HttpServletResponse.class)));
  }

  @Test
  public void givenIsAuthenticated_whenUpdating_thenReturnsUpdated() throws Exception {
    String currentEmail = "123@456.com", currentPassword = "1234", newEmail = "new@e.mail";

    val student = new Student()
        .setEmail(currentEmail)
        .setPassword(currentPassword)
        .setId(1L)
        .setProfile(new Profile(""));
    TestUtils.authenticate(student);

    val request = new UpdateCredentialsRequest(currentEmail, currentPassword, newEmail, null);

    when(authManager.authenticate(any()))
        .thenReturn(new UsernamePasswordAuthenticationToken(
            newEmail, currentPassword, Set.of(new SimpleGrantedAuthority("User"))));

    when(service.update(any(Student.class), any(), any()))
        .thenReturn(student.setEmail(newEmail));

    assertNotNull(controller.update(request));
  }

  @Test
  public void givenIsAuthenticated_whenDeleting_thenDoesNotThrow() {
    TestUtils.authenticate(new Student().setId(1L));
    assertDoesNotThrow(() -> controller.delete());
  }

}
