package studentscroll.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import studentscroll.api.account.data.Account;
import studentscroll.api.account.data.AccountRepository;

@Service
public class StudentDetailsService implements UserDetailsService {

  @Autowired
  private AccountRepository repo;

  @Override
  public Account loadUserByUsername(String email) throws BadCredentialsException {
    return repo.findByEmail(email)
        .orElseThrow(() -> new BadCredentialsException(""));
  }

}
