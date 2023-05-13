package studentscroll.api.students.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import studentscroll.api.students.data.Settings;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.data.StudentRepository;

@Service
public class SettingsService {

  @Autowired
  private StudentRepository repo;

  public Settings read(
      @NonNull Long studentID) throws EntityNotFoundException {
    return repo
        .findById(studentID)
        .orElseThrow(() -> new EntityNotFoundException("Student does not exist."))
        .getSettings();
  }

  public Settings update(
      @NonNull Long studentID,
      @NonNull Optional<String> newTheme,
      @NonNull Optional<String> newLocale,
      @NonNull Optional<Boolean> newIsLocated) throws EntityNotFoundException {
    Student student = repo
        .findById(studentID)
        .orElseThrow(() -> new EntityNotFoundException("Student does not exist."));

    Settings profile = student.getSettings();
    newTheme.ifPresent(unwrapped -> profile.setTheme(unwrapped));
    newLocale.ifPresent(unwrapped -> profile.setLocale(unwrapped));
    newIsLocated.ifPresent(unwrapped -> profile.setIsLocated(unwrapped));
    student.setSettings(profile);

    return repo.save(student).getSettings();
  }

}