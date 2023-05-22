package studentscroll.api.unit.students;

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
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.students.data.Profile;
import studentscroll.api.students.services.ProfileService;
import studentscroll.api.utils.TestUtils;

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
  public void givenStudentExists_whenReadingProfile_thenReturnsProfile() {
    val profile = TestUtils.getProfile();
    val student = TestUtils.getStudent(1L).setProfile(profile);

    when(repo.findById(any())).thenReturn(Optional.of(student));

    Profile result = service.read(student.getId());

    assertEquals(profile, result);
  }

  @Test
  public void givenStudentDoesNotExist_whenReadingProfile_thenThrowsEntityNotFoundException() {
    when(repo.findById(any())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.read(1L));
  }

  @Test
  public void givenStudentExists_whenUpdatingProfile_thenUpdatesProfile() {
    val profile = TestUtils.getProfile();
    val student = TestUtils.getStudent(1L).setProfile(profile);

    val newName = "new name";

    when(repo.save(any())).thenReturn(student);

    Profile result = service.update(
        student,
        Optional.of(newName),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty());

    assertEquals(newName, result.getName());
  }

}
