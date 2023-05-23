package studentscroll.api.unit.profiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.profiles.data.Profile;
import studentscroll.api.profiles.data.ProfileRepository;
import studentscroll.api.profiles.services.ProfileService;
import studentscroll.api.utils.TestUtils;

public class ProfileServiceTests {

  @Mock
  private ProfileRepository repo;

  @InjectMocks
  private ProfileService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenStudentExists_whenReadingProfile_thenReturnsProfile() {
    val profile = TestUtils.getProfile(1L);

    when(repo.findById(any())).thenReturn(Optional.of(profile));

    Profile result = service.read(profile.getId());

    assertEquals(profile, result);
  }

  @Test
  public void givenStudentDoesNotExist_whenReadingProfile_thenThrowsEntityNotFoundException() {
    when(repo.findById(any())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.read(1L));
  }

  @Test
  public void givenStudentExists_whenUpdatingProfile_thenUpdatesProfile() {
    val profile = TestUtils.getProfile(1L);

    val newName = "new name";

    when(repo.save(any())).thenReturn(profile);

    Profile result = service.update(
        profile,
        Optional.of(newName),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty());

    assertEquals(newName, result.getName());
  }

}
