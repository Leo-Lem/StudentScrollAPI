package studentscroll.api.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.shared.Location;
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
  public void givenProfileExists_whenReadingProfile_thenReturnsCorrectProfile() {
    Student student = exampleStudent();
    Profile example = exampleProfile();
    student.setProfile(example);

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    Profile profile = service.readProfile(student.getId());

    assertEquals(example, profile);
  }

  @Test
  public void givenProfileDoesNotExist_whenReadingProfile_thenThrowsEntityNotFoundException() {
    Long studentID = 1L;

    when(repo.findById(studentID))
        .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.readProfile(studentID));
  }

  @Test
  public void givenProfileExists_whenUpdatingAllProfileInformation_thenAllProfileInformationIsUpdated() {
    Student student = exampleStudent();
    Profile example = exampleProfile();

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Profile profile = service.updateProfile(
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
  public void givenProfileExists_whenUpdatingOnlySomeInformation_thenOnlyThatInformationIsUpdated() {
    Student student = exampleStudent();
    Profile example = exampleProfile();

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Profile profile = service.updateProfile(
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
            portrayed wearing a pair of jeans and an undershirt. He resides with his family on
            Rural Route 9 in Springfield. He is married to Brandine Spuckler, and it is heavily implied
            throughout the series that they are also brother and sister
            or in some way related (possibly cousins), making their children together inbred.
            """,
        "PIGS",
        Set.of("farming", "construction", "redneck"),
        new Location("Rural Route 9, Springfield", 44.046111, -123.021944));
  }

}
