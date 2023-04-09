package studentscroll.api.unit.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import lombok.val;
import studentscroll.api.security.auth.*;
import studentscroll.api.students.data.Profile;
import studentscroll.api.students.data.Student;

public class SigninRestControllerTests {

  @Mock
  private UserDetailsService detailsService;

  @Mock
  private AuthenticationManager authManager;

  @InjectMocks
  private SigninRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenCrendentialsAreValid_whenRequestIsReceived_thenReturns200AndSomeJWT() {
    String email = "abc@xyz.com", password = "1234";
    val details = new Student(email, password, new Profile(""));
    val request = new SigninRequest(email, password);

    when(authManager.authenticate(any(Authentication.class)))
        .thenAnswer(i -> {
          val auth = (Authentication) i.getArguments()[0];
          return new UsernamePasswordAuthenticationToken(
              auth.getPrincipal(), auth.getCredentials(), Set.of(new SimpleGrantedAuthority("USER")));
        });

    when(detailsService.loadUserByUsername(email))
        .thenReturn(details);

    assertTrue(StringUtils.hasText(controller.signin(request).toString()));
  }

  @Test
  public void givenCrendentialsAreInvalid_whenRequestIsReceived_thenThrowsBadCredentialsException() {
    when(authManager.authenticate(any(Authentication.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    assertThrows(BadCredentialsException.class, () -> controller.signin(new SigninRequest("abc@xyz.com", "1234")));
  }

}
