package studentscroll.api.account.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;
import lombok.NonNull;
import studentscroll.api.account.data.Account;
import studentscroll.api.account.data.AccountRepository;
import studentscroll.api.profiles.data.Profile;

@Service
public class AccountService {

  @Autowired
  private AccountRepository repo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public Account create(
      @NonNull String name,
      @NonNull String email,
      @NonNull String password) throws EntityExistsException {
    if (repo.existsByEmail(email))
      throw new EntityExistsException("Email is already in use.");

    Account student = new Account(email, passwordEncoder.encode(password)).setProfile(new Profile(name));

    return repo.save(student);
  }

  public Account update(
      @NonNull Account student,
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