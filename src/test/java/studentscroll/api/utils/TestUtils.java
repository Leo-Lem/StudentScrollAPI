package studentscroll.api.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.students.data.Profile;

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

  public static Student getStudent(Long id) {
    return new Student()
        .setId(id)
        .setEmail("raoul@duke.legend")
        .setPassword("1234")
        .setProfile(new Profile("Raoul Duke"));
  }

  public static Profile getProfile() {
    return new Profile()
        .setName("Cletus Spuckler")
        .setBio("""
            Cletus Delroy Montfort Bigglesworth Spuckler, also known as Cletus the Slack-Jawed Yokel,
            is a stereotypical redneck with a good-natured personality. He is thin and is usually
            portrayed wearing a pair of jeans and an undershirt.
            """)
        .setIcon("PIGS")
        .setInterests(Set.of("farming", "construction", "redneck"))
        .setLocation(Optional.of(new StudentLocation("Rural Route 9, Springfield", 44.046111, -123.021944)));
  }
}