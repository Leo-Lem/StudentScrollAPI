package studentscroll.api.students.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.students.data.StudentRepository;

@Service
public class FollowersService {

  @Autowired
  private StudentRepository repo;

  public Set<Long> readAllFollowers(
      @NonNull Long studentID) throws EntityNotFoundException {
    throw new RuntimeException("Not implemented");
  }

  public Set<Long> readAllFollows(
      @NonNull Long studentID) throws EntityNotFoundException {
    throw new RuntimeException("Not implemented");
  }

  public void follow(
      @NonNull Long studentID,
      @NonNull Long followerID) throws EntityNotFoundException {
    throw new RuntimeException("Not implemented");
  }

  public void unfollow(
      @NonNull Long studentID,
      @NonNull Long followerID) throws EntityNotFoundException {
    throw new RuntimeException("Not implemented");
  }

}
