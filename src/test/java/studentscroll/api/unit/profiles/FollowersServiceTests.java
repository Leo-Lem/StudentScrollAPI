package studentscroll.api.unit.profiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.profiles.data.ProfileRepository;
import studentscroll.api.profiles.services.FollowersService;
import studentscroll.api.utils.TestUtils;

public class FollowersServiceTests {

  @Mock
  private ProfileRepository repo;

  @InjectMocks
  private FollowersService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenStudentExists_whenFollowing_thenAddsToStudentAndReturnsId() {
    val profile = TestUtils.getProfile(1L);
    val follow = TestUtils.getProfile(2L);

    when(repo.findById(follow.getId())).thenReturn(Optional.of(follow));

    val returnedFollow = service.follow(profile, 2L);

    assertTrue(profile.getFollows().contains(follow));
    assertTrue(returnedFollow.getId().equals(follow.getId()));
  }

  @Test
  public void givenStudentExists_whenUnfollowing_thenRemovesFromStudent() {
    val student = TestUtils.getProfile(1L);
    val follow = TestUtils.getProfile(2L);

    student.addFollow(follow);

    when(repo.findById(student.getId())).thenReturn(Optional.of(student));
    when(repo.findById(follow.getId())).thenReturn(Optional.of(follow));

    service.unfollow(student, 2L);

    assertFalse(student.getFollows().contains(follow));
  }

  @Test
  public void givenStudentDoesNotExist_whenFollowing_thenThrowsEntityNotFoundException() {
    val profile = TestUtils.getProfile(1L);

    when(repo.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.follow(profile, 2L));
  }

}
