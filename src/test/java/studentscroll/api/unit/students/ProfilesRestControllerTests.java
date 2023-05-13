package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;

import jakarta.persistence.*;
import lombok.val;
import studentscroll.api.shared.Location;
import studentscroll.api.students.data.*;
import studentscroll.api.students.services.*;
import studentscroll.api.students.web.ProfilesRestController;
import studentscroll.api.students.web.dto.*;

public class ProfilesRestControllerTests {

  @Mock
  private AuthenticationManager authManager;

  @Mock
  private StudentService studentService;

  @Mock
  private ProfileService profileService;

  @InjectMocks
  private ProfilesRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenStudentExists_whenGettingProfile_thenReturnsProfile() {
    val studentID = 1L;
    val profile = exampleProfile();

    when(profileService.read(studentID))
        .thenReturn(profile);

    assertNotNull(controller.readProfile(studentID));
  }

  @Test
  public void givenStudentDoesNotExist_whenGettingUpdatingProfile_thenThrowsEntityNotFoundException() {
    val studentID = 1L;

    when(profileService.read(studentID))
        .thenThrow(new EntityNotFoundException());

    when(profileService.update(anyLong(), any(), any(), any(), any(), any()))
        .thenThrow(new EntityNotFoundException());

    assertThrows(EntityNotFoundException.class, () -> controller.readProfile(studentID));
    assertThrows(EntityNotFoundException.class, () -> controller.updateProfile(
        studentID, new UpdateProfileRequest(null, null, null, null, null)));
  }

  @Test
  public void givenStudentExists_whenUpdatingProfile_thenReturnsUpdatedProfile() {
    val profile = exampleProfile();
    val request = new UpdateProfileRequest(
        profile.getName(),
        profile.getBio(),
        profile.getIcon(),
        profile.getInterests().toArray(new String[] {}),
        profile.getLocation().orElse(null));

    when(profileService.update(anyLong(), any(), any(), any(), any(), any()))
        .thenReturn(profile);

    ProfileResponse response = controller.updateProfile(1L, request);
    assertEquals(profile.getName(), response.getName());
    assertEquals(profile.getBio(), response.getBio());
    assertEquals(profile.getIcon(), response.getIcon());
    assertEquals(profile.getInterests().size(), response.getInterests().length);
    assertEquals(profile.getLocation().orElse(null), response.getLocation());
  }

  @Test
  public void givenStudentExists_whenUpdatingOnlySomeFields_thenLeavesOthersUnchanged() {
    val profile = exampleProfile();
    String name = "John Wayne", icon = "GUN";
    val request = new UpdateProfileRequest(name, null, icon, null, null);

    val updatedProfile = new Profile(
        name, profile.getBio(), icon, profile.getInterests(), profile.getLocation().orElse(null));

    when(profileService.update(anyLong(), any(), any(), any(), any(), any()))
        .thenReturn(updatedProfile);

    ProfileResponse response = controller.updateProfile(1L, request);
    assertEquals(name, response.getName());
    assertEquals(icon, response.getIcon());
    assertEquals(profile.getBio(), response.getBio());
    assertEquals(profile.getInterests().size(), response.getInterests().length);
    assertEquals(profile.getLocation().orElse(null), response.getLocation());
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
