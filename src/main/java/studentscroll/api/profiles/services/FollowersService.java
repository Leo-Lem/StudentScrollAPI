package studentscroll.api.profiles.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.val;
import studentscroll.api.account.data.Account;
import studentscroll.api.account.data.AccountRepository;

@Service
public class FollowersService {

  @Autowired
  private AccountRepository repo;

  public Set<Long> readFollowers(
      @NonNull Long studentID) throws EntityNotFoundException {
    val student = repo.findById(studentID).orElseThrow(EntityNotFoundException::new);

    return student.getProfile().getFollowers().stream().map(Account::getId).collect(Collectors.toSet());
  }

  public Set<Long> readFollows(
      @NonNull Long studentID) throws EntityNotFoundException {
    val student = repo.findById(studentID).orElseThrow(EntityNotFoundException::new);
    return student.getProfile().getFollows().stream().map(Account::getId).collect(Collectors.toSet());
  }

  public Long follow(
      @NonNull Account student,
      @NonNull Long followId) throws EntityNotFoundException, EntityExistsException, IllegalArgumentException {
    if (student.getId().equals(followId))
      throw new IllegalArgumentException("Student cannot follow themselves");

    val follow = repo.findById(followId).orElseThrow(EntityNotFoundException::new);

    if (student.getProfile().getFollows().contains(follow))
      throw new EntityExistsException();

    student.getProfile().getFollows().add(follow);
    repo.save(student);
    return followId;
  }

  public void unfollow(
      @NonNull Account student,
      @NonNull Long followId) throws EntityNotFoundException {
    val follow = repo.findById(followId).orElseThrow(EntityNotFoundException::new);

    if (!student.getProfile().getFollows().contains(follow))
      throw new EntityNotFoundException();

    student.getProfile().getFollows().remove(follow);
    repo.save(student);
  }

}
