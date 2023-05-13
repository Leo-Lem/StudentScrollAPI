package studentscroll.api.students.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.data.StudentRepository;

@Service
public class FollowersService {

  @Autowired
  private StudentRepository repo;

  public Set<Long> readAllFollowers(
      @NonNull Long studentID) throws EntityNotFoundException {
    val student = repo.findById(studentID).orElseThrow(EntityNotFoundException::new);
    return student.getProfile().getFollowers().stream().map(Student::getId).collect(Collectors.toSet());
  }

  public Set<Long> readAllFollows(
      @NonNull Long studentID) throws EntityNotFoundException {
    val student = repo.findById(studentID).orElseThrow(EntityNotFoundException::new);
    return student.getProfile().getFollows().stream().map(Student::getId).collect(Collectors.toSet());
  }

  public Long follow(
      @NonNull Long studentID,
      @NonNull Long followerID) throws EntityNotFoundException {
    val student = repo.findById(studentID).orElseThrow(EntityNotFoundException::new);
    val follower = repo.findById(followerID).orElseThrow(EntityNotFoundException::new);
    student.getProfile().getFollowers().add(follower);
    return repo.save(student).getId();
  }

  public void unfollow(
      @NonNull Long studentID,
      @NonNull Long followerID) throws EntityNotFoundException {
    val student = repo.findById(studentID).orElseThrow(EntityNotFoundException::new);
    val follower = repo.findById(followerID).orElseThrow(EntityNotFoundException::new);
    student.getProfile().getFollowers().remove(follower);
  }

}
