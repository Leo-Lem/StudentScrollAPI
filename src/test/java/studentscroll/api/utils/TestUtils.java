package studentscroll.api.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.val;
import studentscroll.api.students.data.Student;

public class TestUtils {

  public static void authenticate(Student student) {
    val context = mock(SecurityContext.class);
    val authentication = mock(Authentication.class);

    when(context.getAuthentication())
        .thenReturn(authentication);

    when(authentication.getPrincipal())
        .thenReturn(student);

    SecurityContextHolder.setContext(context);
  }

  public static void unauthenticate() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }

}
