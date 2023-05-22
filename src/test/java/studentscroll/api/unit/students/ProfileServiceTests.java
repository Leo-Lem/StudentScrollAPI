package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.account.data.Student;
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.students.data.*;
import studentscroll.api.students.services.ProfileService;

public class ProfileServiceTests {

  @Mock
  private StudentRepository repo;

  @InjectMocks
  private ProfileService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private Student exampleStudent() {
    return new Student("", "", new Profile("James")).setId(1L);
  }

  private Profile exampleProfile() {
    return new Profile(
        "Cletus Spuckler",
        """
            Cletus Delroy Montfort Bigglesworth Spuckler, also known as Cletus the Slack-Jawed Yokel,
            is a stereotypical redneck with a good-natured personality. He is thin and is usually
            portrayed wearing a pair of jeans and an undershirt.
            """,
        "PIGS",
        Set.of("farming", "construction", "redneck"),
        new ArrayList<>(),
        new ArrayList<>(),
        new StudentLocation("Rural Route 9, Springfield", 44.046111, -123.021944));
  }

}
