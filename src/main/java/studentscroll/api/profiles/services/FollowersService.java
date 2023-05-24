package studentscroll.api.profiles.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.val;
import studentscroll.api.profiles.data.Profile;
import studentscroll.api.profiles.data.ProfileRepository;

@Service
public class FollowersService {

  @Autowired
  private ProfileRepository repo;

  public Profile follow(
      @NonNull Profile profile,
      @NonNull Long followId) throws EntityNotFoundException, EntityExistsException, IllegalArgumentException {
    if (profile.getId().equals(followId))
      throw new IllegalArgumentException("Student cannot follow themselves");

    val follow = repo.findById(followId).orElseThrow(EntityNotFoundException::new);

    if (profile.getFollows().stream().anyMatch(f -> f.getId().equals(followId)))
      throw new EntityExistsException();

    profile.addFollow(follow);
    repo.save(profile);
    return follow;
  }

  public void unfollow(
      @NonNull Profile profile,
      @NonNull Long followId) throws EntityNotFoundException {
    val follow = repo.findById(followId).orElseThrow(EntityNotFoundException::new);

    if (!profile.getFollows().stream().anyMatch(f -> f.getId().equals(followId)))
      throw new EntityNotFoundException();

    repo.save(profile.removeFollow(follow));
  }

}
