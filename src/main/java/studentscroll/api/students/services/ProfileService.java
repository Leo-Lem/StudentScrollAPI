package studentscroll.api.students.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import studentscroll.api.account.data.Student;
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.students.data.*;

@Service
public class ProfileService {

  @Autowired
  private ProfileRepository repo;

  @Autowired
  private StudentRepository studentRepo;

  public Profile read(
      @NonNull Long studentID) throws EntityNotFoundException {
    return studentRepo
        .findById(studentID)
        .orElseThrow(EntityNotFoundException::new)
        .getProfile();
  }

  public Profile update(
      @NonNull Student student,
      @NonNull Optional<String> newName,
      @NonNull Optional<String> newBio,
      @NonNull Optional<String> newIcon,
      @NonNull Optional<Set<String>> newInterests,
      @NonNull Optional<StudentLocation> newLocation) {
    Profile profile = student.getProfile();
    newName.ifPresent(unwrapped -> profile.setName(unwrapped));
    newBio.ifPresent(unwrapped -> profile.setBio(unwrapped));
    newIcon.ifPresent(unwrapped -> profile.setIcon(unwrapped));
    newInterests.ifPresent(unwrapped -> profile.setInterests(unwrapped));
    newLocation.ifPresent(unwrapped -> profile.setLocation(Optional.of(unwrapped)));
    student.setProfile(profile);

    return studentRepo.save(student).getProfile();
  }

  public List<Profile> readAllNearLocation(StudentLocation location) {
    double radiusInKm = 10.0; // Fixed radius of 10km

    double latitudeRange = radiusInKm * 0.009; // Roughly 1km in latitude
    double longitudeRange = radiusInKm * 0.014; // Roughly 1km in longitude

    double minLatitude = location.getLatitude() - latitudeRange;
    double maxLatitude = location.getLatitude() + latitudeRange;
    double minLongitude = location.getLongitude() - longitudeRange;
    double maxLongitude = location.getLongitude() + longitudeRange;

    return repo.findNearLocation(minLatitude, maxLatitude, minLongitude, maxLongitude);
  }

  public List<Profile> readByName(@NonNull String name) {
    return repo.findByName(name);
  }

  public List<Profile> readByInterests(@NonNull Set<String> interests) {
    return repo.findByInterestsIn(interests);
  }

  public List<Profile> readAll() {
    return repo.findAll();
  }

}