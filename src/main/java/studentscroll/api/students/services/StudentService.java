package studentscroll.api.students.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.*;
import lombok.NonNull;
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
      throw new EntityExistsException();

    return repo.save(new Student(email, passwordEncoder.encode(password), new Profile(name)));
  }

  public Student read(@NonNull Long studentId) throws EntityNotFoundException {
    return repo
        .findById(studentId)
        .orElseThrow(() -> new EntityNotFoundException());
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

  public void delete(@NonNull Long studentId) throws EntityNotFoundException {
    if (!repo.existsById(studentId))
      throw new EntityNotFoundException();

    repo.deleteById(studentId);
  }

}