package studentscroll.api.unit.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lombok.val;
import studentscroll.api.account.data.Settings;
import studentscroll.api.account.data.Student;
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.account.services.SettingsService;
import studentscroll.api.students.data.Profile;
import studentscroll.api.utils.TestUtils;

public class SettingsServiceTests {

  @Mock
  private StudentRepository repo;

  @InjectMocks
  private SettingsService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenIsAuthenticated_whenReadingSettings_thenReturnsCorrectSettings() {
    Student student = exampleStudent().setSettings(exampleSettings());

    TestUtils.authenticate(student);

    Settings Settings = service.read();

    assertEquals(student.getSettings(), Settings);
  }

  @Test
  public void givenIsAuthenticated_whenUpdatingSettings_thenReturnsCorrectSettings() {
    val student = exampleStudent().setSettings(exampleSettings());

    TestUtils.authenticate(student);

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Settings newSettings = service.update(
        Optional.of("DARK"),
        Optional.empty());

    assertNotEquals("LIGHT", newSettings.getTheme());
    assertEquals("DE", newSettings.getLocale());
  }

  private Student exampleStudent() {
    return new Student("", "", new Profile("James")).setId(1L);
  }

  private Settings exampleSettings() {
    return new Settings("LIGHT", "DE");
  }

}
