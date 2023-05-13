package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.students.services.FollowersService;
import studentscroll.api.students.web.FollowersRestController;

public class FollowersRestControllerTests {

  @Mock
  private FollowersService service;

  @InjectMocks
  private FollowersRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenStudentExists_whenFollowing_thenReturnsFollowerId() {
    val studentId = 1L;
    val followerId = 2L;

    when(service.follow(studentId, followerId))
        .thenReturn(followerId);

    val response = controller.follow(studentId, followerId);

    assertEquals(followerId, response);
  }

  @Test
  public void givenStudentExists_whenGettingFollowers_thenReturnsFollowers() {
    val studentId = 1L;
    val followerIds = Set.of(2L, 3L, 4L);

    when(service.readAllFollowers(studentId))
        .thenReturn(followerIds);

    val followers = controller.readAllFollowers(studentId);

    assertEquals(followerIds, followers);
  }

  @Test
  public void givenStudentExists_whenGettingFollows_thenReturnsFollows() {
    val studentId = 1L;
    val followIds = Set.of(2L, 3L, 4L);

    when(service.readAllFollows(studentId))
        .thenReturn(followIds);

    val follows = controller.readAllFollows(studentId);

    assertEquals(followIds, follows);
  }

  @Test
  public void givenStudentExists_whenUnfollowing_thenDoesNotThrow() {
    val studentId = 1L;
    val followerId = 2L;

    assertDoesNotThrow(() -> controller.unfollow(studentId, followerId));
  }

  @Test
  public void givenStudentDoesNotExist_whenGettingFollowersOrGettingFollowsOrFollowingOrUnfollowing_thenReturnsNotFound() {
    val studentId = 1L;
    val followerId = 2L;

    when(service.follow(studentId, followerId))
        .thenThrow(new EntityNotFoundException());

    assertThrows(EntityNotFoundException.class, () -> controller.follow(studentId, followerId));

    when(service.readAllFollowers(studentId))
        .thenThrow(new EntityNotFoundException());

    assertThrows(EntityNotFoundException.class, () -> controller.readAllFollowers(studentId));

    when(service.readAllFollows(studentId))
        .thenThrow(new EntityNotFoundException());

    assertThrows(EntityNotFoundException.class, () -> controller.readAllFollows(studentId));

    assertDoesNotThrow(() -> controller.unfollow(studentId, followerId));
  }

}
