package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.shared.Location;
import studentscroll.api.students.data.*;
import studentscroll.api.students.services.*;
import studentscroll.api.students.web.StudentsRestController;
import studentscroll.api.students.web.dto.*;

public class StudentsRestControllerTests {

  @Mock
  private StudentService studentService;

  @Mock
  private ProfileService profileService;

  @InjectMocks
  private StudentsRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenEmailIsNotRegistered_whenRegistering_thenReturns200AndStudent() {
    String name = "Xavier Yoolan", email = "xyz@abc.com", password = "4321";
    val request = new CreateStudentRequest(name, email, password);

    when(studentService.create(name, email, password))
        .thenReturn(new Student(email, "abc123", new Profile(name)).setId(1L));

    ResponseEntity<?> response = controller.registerStudent(request);

    assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(200));
    StudentResponse body = (StudentResponse) response.getBody();
    if (body != null) {
      assertNotNull(body.getId());
      assertEquals(name, body.getName());
      assertEquals(email, body.getEmail());
    }
  }

  @Test
  public void givenEmailIsRegistered_whenRegistering_thenReturns409() {
    String email = "xyz@abc.com";
    val request = new CreateStudentRequest("", email, "");

    when(studentService.create("", email, ""))
        .thenThrow(new EntityExistsException());

    ResponseEntity<?> response = controller.registerStudent(request);

    assertEquals(HttpStatusCode.valueOf(409), response.getStatusCode());
  }

  @Test
  public void givenStudentExists_whenGettingProfile_thenReturns200AndProfile() {
    val studentID = 1L;
    val profile = exampleProfile();

    when(profileService.read(studentID))
        .thenReturn(profile);

    ResponseEntity<?> response = controller.readProfile(studentID);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    assertNotNull((ProfileResponse) response.getBody());
  }

  @Test
  public void givenStudentDoesNotExist_whenGettingProfile_thenReturns404() {
    val studentID = 1L;

    when(profileService.read(studentID))
        .thenThrow(new EntityNotFoundException());

    ResponseEntity<?> response = controller.readProfile(studentID);

    assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
  }

  @Test
  public void givenStudentExists_whenUpdatingProfile_thenReturns200AndUpdatedProfile() {
    val profile = exampleProfile();
    val request = new UpdateProfileRequest(
        profile.getName(),
        profile.getBio(),
        profile.getIcon(),
        profile.getInterests().toArray(new String[] {}),
        profile.getLocation().orElse(null));

    when(profileService.update(anyLong(), any(), any(), any(), any(), any()))
        .thenReturn(profile);

    ResponseEntity<?> response = controller.updateProfile(1L, request);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    ProfileResponse body = (ProfileResponse) response.getBody();
    if (body != null) {
      assertEquals(profile.getName(), body.getName());
      assertEquals(profile.getBio(), body.getBio());
      assertEquals(profile.getIcon(), body.getIcon());
      assertEquals(profile.getInterests().size(), body.getInterests().length);
      assertEquals(profile.getLocation().orElse(null), body.getLocation());
    }
  }

  @Test
  public void givenStudentExists_whenUpdatingOnlySomeFields_leavesOthersUnchanged() {
    val profile = exampleProfile();
    String name = "John Wayne", icon = "GUN";
    val request = new UpdateProfileRequest(name, null, icon, null, null);

    val updatedProfile = new Profile(
        name, profile.getBio(), icon, profile.getInterests(), profile.getLocation().orElse(null));

    when(profileService.update(anyLong(), any(), any(), any(), any(), any()))
        .thenReturn(updatedProfile);

    ResponseEntity<?> response = controller.updateProfile(1L, request);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    ProfileResponse body = (ProfileResponse) response.getBody();
    if (body != null) {
      assertEquals(name, body.getName());
      assertEquals(icon, body.getIcon());
      assertEquals(profile.getBio(), body.getBio());
      assertEquals(profile.getInterests().size(), body.getInterests().length);
      assertEquals(profile.getLocation().orElse(null), body.getLocation());
    }
  }

  @Test
  public void givenStudentDoesNotExist_whenUpdatingProfile_thenReturns404() {
    val studentID = 1L;

    when(profileService.update(anyLong(), any(), any(), any(), any(), any()))
        .thenThrow(new EntityNotFoundException());

    ResponseEntity<?> response = controller.updateProfile(studentID,
        new UpdateProfileRequest(null, null, null, null, null));

    assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
  }

  private Profile exampleProfile() {
    return new Profile(
        "James Bond",
        "James Bond is a fictional character created by British novelist Ian Fleming in 1953.",
        "SPORTS_CAR",
        Set.of("SPY", "CARS", "WEAPONS"),
        new Location("MI6 building", 51.487222, -0.124167));
  }

}
