package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.students.data.*;
import studentscroll.api.students.services.SettingsService;

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
  public void givenStudentExists_whenReadingSettings_thenReturnsCorrectSettings() {
    Student student = exampleStudent();
    Settings example = exampleSettings();
    student.setSettings(example);

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    Settings Settings = service.read(student.getId());

    assertEquals(example, Settings);
  }

  @Test
  public void givenStudentDoesNotExist_whenReadingSettings_thenThrowsEntityNotFoundException() {
    Long studentID = 1L;

    when(repo.findById(studentID))
        .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.read(studentID));
  }

  @Test
  public void givenStudentExists_whenUpdatingAllSettingsInformation_thenAllSettingsInformationIsUpdated() {
    Student student = exampleStudent();
    Settings example = exampleSettings();

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Settings Settings = service.update(
        student.getId(),
        Optional.of(example.getTheme()),
        Optional.of(example.getLocale()));

    assertEquals(example.getTheme(), Settings.getTheme());
    assertEquals(example.getLocale(), Settings.getLocale());
  }

  @Test
  public void givenStudentExists_whenUpdatingOnlySomeInformation_thenOnlyThatInformationIsUpdated() {
    Student student = exampleStudent();
    Settings example = exampleSettings();

    when(repo.findById(student.getId()))
        .thenReturn(Optional.of(student));

    when(repo.save(any(Student.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Settings Settings = service.update(
        student.getId(),
        Optional.of(example.getTheme()),
        Optional.empty());

    assertEquals(example.getTheme(), Settings.getTheme());
    assertNotEquals(example.getLocale(), Settings.getLocale());
  }

  private Student exampleStudent() {
    return new Student("", "", new Profile("James")).setId(1L);
  }

  private Settings exampleSettings() {
    return new Settings("LIGHT", "DE");
  }

}
