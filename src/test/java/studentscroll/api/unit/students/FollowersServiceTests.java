package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.students.data.*;
import studentscroll.api.students.services.FollowersService;

public class FollowersServiceTests {

  @Mock
  private StudentRepository repo;

  @InjectMocks
  private FollowersService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenStudentExists_whenReadingAllFollowers_thenReturnsCorrectFollowers() {
    val student = exampleStudent();
    val followers = List.of(exampleStudent(), exampleStudent());
    student.getProfile().setFollowers(followers);

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    val result = service.readAllFollowers(student.getId());

    assertEquals(result.size(), followers.size());

    for (val follower : followers)
      assertTrue(result.contains(follower.getId()));
  }

  @Test
  public void givenStudentExists_whenReadingAllFollows_thenReturnsCorrectFollows() {
    val student = exampleStudent();
    val follows = List.of(exampleStudent(), exampleStudent());
    student.getProfile().setFollows(follows);

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    val result = service.readAllFollows(student.getId());

    assertEquals(result.size(), follows.size());

    for (val follow : follows)
      assertTrue(result.contains(follow.getId()));
  }

  @Test
  public void givenStudentExists_whenFollowing_thenFollowsCorrectly() {
    val student = exampleStudent();
    val follower = exampleStudent();

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    when(repo.findById(follower.getId()))
        .thenReturn(Optional.of(follower));

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    service.follow(student.getId(), follower.getId());

    assertTrue(student.getProfile().getFollowers().contains(follower));
  }

  @Test
  public void givenStudentExists_whenUnfollowing_thenUnfollowsCorrectly() {
    val student = exampleStudent();
    val follower = exampleStudent();
    student.getProfile().getFollowers().add(follower);

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    when(repo.findById(follower.getId()))
        .thenReturn(Optional.of(follower));

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    service.unfollow(student.getId(), follower.getId());

    assertFalse(student.getProfile().getFollowers().contains(follower));
  }

  @Test
  public void givenStudentDoesNotExist_whenReadingFollowersOrReadingFollowsOrFollowingOrUnfollowing_thenThrowsEntityNotFoundException() {
    when(repo.findById(any(Long.class)))
        .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.readAllFollowers(1L));
    assertThrows(EntityNotFoundException.class, () -> service.readAllFollows(1L));
    assertThrows(EntityNotFoundException.class, () -> service.follow(1L, 1L));
    assertThrows(EntityNotFoundException.class, () -> service.unfollow(1L, 1L));
  }

  private Student exampleStudent() {
    return new Student("raoul@duke.legend", "1234", new Profile("Raoul Duke")).setId(new Random().nextLong());
  }

}
