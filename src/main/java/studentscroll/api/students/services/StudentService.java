package studentscroll.api.students.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import studentscroll.api.students.data.*;

@Service
public class StudentService {

  @Autowired
  private StudentRepository repo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Student create(String name, String email, String password) throws EntityExistsException {
    if (repo.existsByEmail(email))
      throw new EntityExistsException();

    return repo.save(new Student(email, passwordEncoder.encode(password), new Profile(name)));
  }

}