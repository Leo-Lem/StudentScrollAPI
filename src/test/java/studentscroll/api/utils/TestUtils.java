package studentscroll.api.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.val;
import studentscroll.api.account.data.Account;
import studentscroll.api.account.data.Settings;
import studentscroll.api.posts.data.ContentPost;
import studentscroll.api.posts.data.Post;
import studentscroll.api.profiles.data.Profile;
import studentscroll.api.shared.StudentLocation;

public class TestUtils {

  public static void authenticate(Account student) {
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

  public static Account getStudent(Long id) {
    return new Account()
        .setId(id)
        .setEmail("raoul@duke.legend")
        .setPassword("1234")
        .setProfile(new Profile("Raoul Duke").setId(1L));
  }

  public static Profile getProfile(Long id) {
    return new Profile()
        .setId(id)
        .setName("Cletus Spuckler")
        .setBio("""
            Cletus Delroy Montfort Bigglesworth Spuckler, also known as Cletus the Slack-Jawed Yokel,
            is a stereotypical redneck with a good-natured personality. He is thin and is usually
            portrayed wearing a pair of jeans and an undershirt.
            """)
        .setIcon("PIGS")
        .setInterests(List.of("farming", "construction", "redneck"))
        .setLocation(Optional.of(new StudentLocation("Rural Route 9, Springfield", 44.046111, -123.021944)));
  }

  public static Settings getSettings() {
    return new Settings()
        .setTheme("LIGHT")
        .setLocale("DE");
  }

  public static Post getPost() {
    return new ContentPost()
        .setContent("Jimmy's dog is really cute. I would love to pet it again.")
        .setTitle("Jimmy's Dog")
        .setTags(Set.of("JIMMY", "DOG"))
        .setPoster(new Account().setId(1L))
        .setId(1L);
  }
}