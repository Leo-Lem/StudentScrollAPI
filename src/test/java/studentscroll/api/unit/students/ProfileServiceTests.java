package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import jakarta.persistence.EntityNotFoundException;
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

  @Test
  public void givenStudentExists_whenReadingProfile_thenReturnsCorrectProfile() {
    Student student = exampleStudent();
    Profile example = exampleProfile();
    student.setProfile(example);

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    Profile profile = service.read(student.getId());

    assertEquals(example, profile);
  }

  @Test
  public void givenStudentDoesNotExist_whenReadingProfile_thenThrowsEntityNotFoundException() {
    Long studentID = 1L;

    when(repo.findById(studentID))
        .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.read(studentID));
  }

  @Test
  public void givenStudentExists_whenUpdatingAllProfileInformation_thenAllProfileInformationIsUpdated() {
    Student student = exampleStudent();
    Profile example = exampleProfile();

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Profile profile = service.update(
        student.getId(),
        Optional.of(example.getName()),
        Optional.of(example.getBio()),
        Optional.of(example.getIcon()),
        Optional.of(example.getInterests()),
        example.getLocation());

    assertEquals(example.getName(), profile.getName());
    assertEquals(example.getBio(), profile.getBio());
    assertEquals(example.getIcon(), profile.getIcon());
    assertEquals(example.getInterests(), profile.getInterests());
    assertEquals(example.getLocation(), profile.getLocation());
  }

  @Test
  public void givenStudentExists_whenUpdatingOnlySomeInformation_thenOnlyThatInformationIsUpdated() {
    Student student = exampleStudent();
    Profile example = exampleProfile();

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Profile profile = service.update(
        student.getId(),
        Optional.of(example.getName()),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        example.getLocation());

    assertEquals(example.getName(), profile.getName());
    assertNotEquals(example.getBio(), profile.getBio());
    assertNotEquals(example.getIcon(), profile.getIcon());
    assertNotEquals(example.getInterests(), profile.getInterests());
    assertEquals(example.getLocation(), profile.getLocation());
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
