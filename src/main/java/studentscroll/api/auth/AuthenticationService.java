package studentscroll.api.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import lombok.NonNull;
import lombok.val;
import studentscroll.api.students.data.Profile;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.data.StudentRepository;

@Service
public class AuthenticationService {

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

  public Student readCurrent() throws IllegalStateException {
    val student = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    System.out.println(student);
    if (student == null)
      throw new IllegalStateException("Not authenticated.");
    return student;
  }

  public Student update(
      @NonNull Optional<String> newEmail,
      @NonNull Optional<String> newPassword) throws IllegalStateException {
    Student student = readCurrent();

    newEmail.ifPresent(unwrapped -> student.setEmail(unwrapped));
    newPassword.ifPresent(unwrapped -> student.setPassword(passwordEncoder.encode(unwrapped)));

    return repo.save(student);
  }

  public void delete() throws IllegalStateException {
    val studentId = readCurrent().getId();
    repo.deleteById(studentId);
  }

}