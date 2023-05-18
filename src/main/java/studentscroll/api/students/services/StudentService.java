package studentscroll.api.students.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.*;
import lombok.NonNull;
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.students.data.*;

@Service
public class StudentService {

  @Autowired
  private StudentRepository repo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Student create(
      @NonNull String name,
      @NonNull String email,
      @NonNull String password) throws EntityExistsException {
    if (repo.existsByEmail(email))
      throw new EntityExistsException("Email is already in use.");

    return repo.save(new Student(email, passwordEncoder.encode(password), new Profile(name)));
  }

  public Student read(
      @NonNull Long studentId) throws EntityNotFoundException {
    return repo
        .findById(studentId)
        .orElseThrow(EntityNotFoundException::new);
  }

  public Student update(
      @NonNull Long studentId,
      @NonNull Optional<String> newEmail,
      @NonNull Optional<String> newPassword) throws EntityNotFoundException {
    Student student = read(studentId);
    newEmail.ifPresent(unwrapped -> student.setEmail(unwrapped));
    newPassword.ifPresent(unwrapped -> student.setPassword(passwordEncoder.encode(unwrapped)));
    return repo.save(student);
  }

  public void delete(
      @NonNull Long studentId) throws EntityNotFoundException {
    if (!repo.existsById(studentId))
      throw new EntityNotFoundException();

    repo.deleteById(studentId);
  }

  public Set<Student> readAllNearLocation(StudentLocation location) {
    double radiusInKm = 10.0; // Fixed radius of 10km

    double latitudeRange = radiusInKm * 0.009; // Roughly 1km in latitude
    double longitudeRange = radiusInKm * 0.014; // Roughly 1km in longitude

    double minLatitude = location.getLatitude() - latitudeRange;
    double maxLatitude = location.getLatitude() + latitudeRange;
    double minLongitude = location.getLongitude() - longitudeRange;
    double maxLongitude = location.getLongitude() + longitudeRange;

    return repo.findStudentsNearLocation(minLatitude, maxLatitude, minLongitude, maxLongitude);
  }

}