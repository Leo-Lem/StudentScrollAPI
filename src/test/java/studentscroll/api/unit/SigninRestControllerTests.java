package studentscroll.api.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import lombok.val;
import studentscroll.api.security.auth.*;
import studentscroll.api.students.data.Student;

public class SigninRestControllerTests {

  @Mock
  private UserDetailsService detailsService;

  @Mock
  private AuthenticationManager authManager;

  @InjectMocks
  private SigninRestController controller;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenCrendentialsAreValid_whenRequestIsReceived_thenReturnsSomeJWT() {
    String email = "abc@xyz.com", password = "1234";
    val details = Student.builder().email(email).password(password).build();
    val request = new SigninRequest(email, password);

    when(authManager.authenticate(any(Authentication.class)))
        .thenAnswer(i -> {
          val auth = (Authentication) i.getArguments()[0];
          return new UsernamePasswordAuthenticationToken(
              auth.getPrincipal(), auth.getCredentials(), Set.of(new SimpleGrantedAuthority("USER")));
        });

    when(detailsService.loadUserByUsername(email))
        .thenReturn(details);

    ResponseEntity<?> response = controller.signin(request);

    assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(200));

    val body = response.getBody();
    if (body != null)
      assertTrue(StringUtils.hasText(body.toString()));
    else
      fail("No JWT returned");
  }

  @Test
  public void givenCrendentialsAreInvalid_whenRequestIsReceived_thenReturns401() {
    when(authManager.authenticate(any(Authentication.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    ResponseEntity<?> response = controller.signin(new SigninRequest("abc@xyz.com", "1234"));

    assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(401));
  }

}
