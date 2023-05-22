package studentscroll.api.students.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.account.data.StudentRepository;

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
      @NonNull Long followerID) throws EntityNotFoundException, EntityExistsException, IllegalArgumentException {
    if (studentID.equals(followerID))
      throw new IllegalArgumentException("Student cannot follow themselves");

    val student = repo.findById(studentID).orElseThrow(EntityNotFoundException::new);
    val follower = repo.findById(followerID).orElseThrow(EntityNotFoundException::new);

    if (student.getProfile().getFollowers().contains(follower))
      throw new EntityExistsException();

    student.getProfile().getFollowers().add(follower);
    repo.save(student);
    return followerID;
  }

  public void unfollow(
      @NonNull Long studentID,
      @NonNull Long followerID) throws EntityNotFoundException {
    val student = repo.findById(studentID).orElseThrow(EntityNotFoundException::new);
    val follower = repo.findById(followerID).orElseThrow(EntityNotFoundException::new);

    if (!student.getProfile().getFollowers().contains(follower))
      throw new EntityNotFoundException();

    student.getProfile().getFollowers().remove(follower);
    repo.save(student);
  }

}
