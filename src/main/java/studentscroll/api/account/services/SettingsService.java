package studentscroll.api.account.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import studentscroll.api.account.data.Settings;
import studentscroll.api.account.data.Account;
import studentscroll.api.account.data.AccountRepository;

@Service
public class SettingsService {

  @Autowired
  private AccountRepository repo;

  public Settings read(@NonNull Account student) throws IllegalStateException {
    return student.getSettings();
  }

  public Settings update(
      @NonNull Account student,
      @NonNull Optional<String> newTheme,
      @NonNull Optional<String> newLocale) throws IllegalStateException {
    Settings settings = student.getSettings();
    newTheme.ifPresent(unwrapped -> settings.setTheme(unwrapped));
    newLocale.ifPresent(unwrapped -> settings.setLocale(unwrapped));
    student.setSettings(settings);

    return repo.save(student).getSettings();
  }

}