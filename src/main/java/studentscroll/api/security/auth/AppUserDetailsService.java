package studentscroll.api.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.data.repos.StudentRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

  @Autowired
  StudentRepository studentRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Student student = studentRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));

    return student;
  }

}