package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.students.services.FollowersService;
import studentscroll.api.utils.TestUtils;

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
  public void givenStudentExists_whenReadingAllFollowers_thenReturnsSetOfFollowers() {
    val followers = List.of(
        TestUtils.getStudent(2L),
        TestUtils.getStudent(3L));
    val student = TestUtils.getStudent(1L);
    student.getProfile().setFollowers(followers);

    when(repo.findById(1L)).thenReturn(Optional.of(student));

    Set<Long> result = service.readFollowers(1L);

    assertTrue(result.contains(2L));
    assertTrue(result.contains(3L));
  }

  @Test
  public void givenStudentExists_whenReadingAllFollows_thenReturnsSetOfFollows() {
    val follows = List.of(
        TestUtils.getStudent(2L),
        TestUtils.getStudent(3L));
    val student = TestUtils.getStudent(1L);
    student.getProfile().setFollows(follows);

    when(repo.findById(1L)).thenReturn(Optional.of(student));

    Set<Long> result = service.readFollows(1L);

    assertTrue(result.contains(2L));
    assertTrue(result.contains(3L));
  }

  @Test
  public void givenStudentExists_whenFollowing_thenAddsToStudentAndReturnsId() {
    val student = TestUtils.getStudent(1L);
    val follow = TestUtils.getStudent(2L);

    when(repo.findById(student.getId())).thenReturn(Optional.of(student));
    when(repo.findById(follow.getId())).thenReturn(Optional.of(follow));

    val followId = service.follow(student, 2L);

    assertTrue(student.getProfile().getFollows().contains(follow));
    assertTrue(followId.equals(follow.getId()));
  }

  @Test
  public void givenStudentExists_whenUnfollowing_thenRemovesFromStudent() {
    val student = TestUtils.getStudent(1L);
    val follow = TestUtils.getStudent(2L);

    student.getProfile().getFollows().add(follow);

    when(repo.findById(student.getId())).thenReturn(Optional.of(student));
    when(repo.findById(follow.getId())).thenReturn(Optional.of(follow));

    service.unfollow(student, 2L);

    assertFalse(student.getProfile().getFollows().contains(follow));
  }

  @Test
  public void givenStudentDoesNotExist_whenFollowing_thenThrowsEntityNotFoundException() {
    val student = TestUtils.getStudent(1L);

    when(repo.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.follow(student, 2L));
  }

}
