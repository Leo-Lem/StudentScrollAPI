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
      @NonNull Optional<String> name,
      @NonNull Optional<String> bio,
      @NonNull Optional<String> icon,
      @NonNull Optional<Set<String>> interests,
      @NonNull Optional<Location> location) throws EntityNotFoundException {

    Student student = repo
        .findById(studentID)
        .orElseThrow(() -> new EntityNotFoundException("Student does not exist."));

    Profile profile = student.getProfile();
    name.ifPresent(unwrapped -> profile.setName(unwrapped));
    bio.ifPresent(unwrapped -> profile.setBio(unwrapped));
    icon.ifPresent(unwrapped -> profile.setIcon(unwrapped));
    interests.ifPresent(unwrapped -> profile.setInterests(unwrapped));
    location.ifPresent(unwrapped -> profile.setLocation(Optional.of(unwrapped)));
    student.setProfile(profile);

    return repo.save(student).getProfile();
  }

}