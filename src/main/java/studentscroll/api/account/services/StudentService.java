package studentscroll.api.account.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import lombok.NonNull;
import studentscroll.api.account.data.Student;
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.students.data.Profile;

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

  public Student update(
      @NonNull Student student,
      @NonNull Optional<String> newEmail,
      @NonNull Optional<String> newPassword) {
    newEmail.ifPresent(unwrapped -> student.setEmail(unwrapped));
    newPassword.ifPresent(unwrapped -> student.setPassword(passwordEncoder.encode(unwrapped)));

    return repo.save(student);
  }

  public void delete(@NonNull Long id) {
    repo.deleteById(id);
  }

}