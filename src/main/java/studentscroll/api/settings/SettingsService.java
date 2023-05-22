package studentscroll.api.settings;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.val;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.data.StudentRepository;

@Service
public class SettingsService {

  @Autowired
  private StudentRepository repo;

  public Settings read() throws IllegalStateException {
    return readCurrentStudent().getSettings();
  }

  public Settings update(
      @NonNull Optional<String> newTheme,
      @NonNull Optional<String> newLocale) throws IllegalStateException {
    val student = readCurrentStudent();

    Settings settings = student.getSettings();
    newTheme.ifPresent(unwrapped -> settings.setTheme(unwrapped));
    newLocale.ifPresent(unwrapped -> settings.setLocale(unwrapped));
    student.setSettings(settings);

    return repo.save(student).getSettings();
  }

  private Student readCurrentStudent() throws IllegalStateException {
    val student = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (student == null)
      throw new IllegalStateException("Not authenticated.");

    return student;
  }

}