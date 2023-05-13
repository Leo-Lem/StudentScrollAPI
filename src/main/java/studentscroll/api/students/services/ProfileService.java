package studentscroll.api.students.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.shared.Location;
import studentscroll.api.students.data.*;

@Service
public class ProfileService {

  @Autowired
  private StudentRepository repo;

  public Profile read(
      @NonNull Long studentID) throws EntityNotFoundException {
    return repo
        .findById(studentID)
        .orElseThrow(() -> new EntityNotFoundException("Student does not exist."))
        .getProfile();
  }

  public Profile update(
      @NonNull Long studentID,
      @NonNull Optional<String> newName,
      @NonNull Optional<String> newBio,
      @NonNull Optional<String> newIcon,
      @NonNull Optional<Set<String>> newInterests,
      @NonNull Optional<Location> newLocation) throws EntityNotFoundException {

    Student student = repo
        .findById(studentID)
        .orElseThrow(() -> new EntityNotFoundException("Student does not exist."));

    Profile profile = student.getProfile();
    newName.ifPresent(unwrapped -> profile.setName(unwrapped));
    newBio.ifPresent(unwrapped -> profile.setBio(unwrapped));
    newIcon.ifPresent(unwrapped -> profile.setIcon(unwrapped));
    newInterests.ifPresent(unwrapped -> profile.setInterests(unwrapped));
    newLocation.ifPresent(unwrapped -> profile.setLocation(Optional.of(unwrapped)));
    student.setProfile(profile);

    return repo.save(student).getProfile();
  }

}