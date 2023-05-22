package studentscroll.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import studentscroll.api.students.data.Student;
import studentscroll.api.students.data.StudentRepository;

@Service
public class StudentDetailsService implements UserDetailsService {

  @Autowired
  private StudentRepository repo;

  @Override
  public Student loadUserByUsername(String email) throws BadCredentialsException {
    return repo.findByEmail(email)
        .orElseThrow(() -> new BadCredentialsException(""));
  }

}
