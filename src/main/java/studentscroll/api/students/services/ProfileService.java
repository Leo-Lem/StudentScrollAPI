package studentscroll.api.students.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.shared.Location;
import studentscroll.api.students.data.*;

@Service
public class ProfileService {

  @Autowired
  private StudentRepository repo;

  public Profile readProfile(Long studentID) throws EntityNotFoundException {
    Student student;
    if ((student = repo.findById(studentID).orElse(null)) == null)
      throw new EntityNotFoundException("Student does not exist.");

    return student.getProfile();
  }

  public Profile updateProfile(
      Long studentID,
      Optional<String> name,
      Optional<String> bio,
      Optional<String> icon,
      Optional<Set<String>> interests,
      Optional<Location> location) throws EntityNotFoundException {

    Student student;
    if ((student = repo.findById(studentID).orElse(null)) == null)
      throw new EntityNotFoundException("Student does not exist.");

    Profile profile = student.getProfile();
    name.ifPresent(unwrapped -> profile.setName(unwrapped));
    bio.ifPresent(unwrapped -> profile.setBio(unwrapped));
    icon.ifPresent(unwrapped -> profile.setIcon(unwrapped));
    interests.ifPresent(unwrapped -> profile.setInterests(unwrapped));
    location.ifPresent(unwrapped -> profile.setLocation(Optional.of(unwrapped)));
    student.setProfile(profile);

    return repo.save(student).getProfile();
  }

  public void readProfile(long l) {
    throw new UnsupportedOperationException("Unimplemented");
  }

}