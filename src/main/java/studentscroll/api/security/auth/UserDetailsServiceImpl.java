package studentscroll.api.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import io.micrometer.common.lang.NonNull;
import jakarta.transaction.Transactional;
import studentscroll.api.students.data.StudentRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  StudentRepository repo;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
    return repo.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));
  }

}