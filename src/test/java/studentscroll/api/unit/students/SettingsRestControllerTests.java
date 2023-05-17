package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.*;
import lombok.val;
import studentscroll.api.students.data.*;
import studentscroll.api.students.services.*;
import studentscroll.api.students.web.SettingsRestController;
import studentscroll.api.students.web.dto.*;

public class SettingsRestControllerTests {

  @Mock
  private SettingsService service;

  @InjectMocks
  private SettingsRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenStudentExists_whenGettingSettings_thenReturnsSettings() {
    val studentID = 1L;
    val settings = exampleSettings();

    when(service.read(studentID))
        .thenReturn(settings);

    assertNotNull(controller.readSettings(studentID));
  }

  @Test
  public void givenStudentDoesNotExist_whenGettingOrUpdatingSettings_thenThrowsEntityNotFoundException() {
    val studentID = 1L;

    when(service.read(studentID))
        .thenThrow(new EntityNotFoundException());

    when(service.update(anyLong(), any(), any()))
        .thenThrow(new EntityNotFoundException());

    assertThrows(EntityNotFoundException.class, () -> controller.readSettings(studentID));
    assertThrows(EntityNotFoundException.class, () -> controller.updateSettings(
        studentID, new UpdateSettingsRequest(null, null)));
  }

  @Test
  public void givenStudentExists_whenUpdatingSettings_thenReturnsUpdatedSettings() {
    val settings = exampleSettings();
    val request = new UpdateSettingsRequest(
        settings.getTheme(),
        settings.getLocale());

    when(service.update(anyLong(), any(), any()))
        .thenReturn(settings);

    SettingsResponse response = controller.updateSettings(1L, request);
    assertEquals(settings.getTheme(), response.getTheme());
    assertEquals(settings.getLocale(), response.getLocale());
  }

  @Test
  public void givenStudentExists_whenUpdatingOnlySomeFields_thenLeavesOthersUnchanged() {
    val settings = exampleSettings();
    String theme = "LIGHT";
    val request = new UpdateSettingsRequest(theme, null);

    val updatedSettings = new Settings(
        theme, settings.getLocale());

    when(service.update(anyLong(), any(), any()))
        .thenReturn(updatedSettings);

    SettingsResponse response = controller.updateSettings(1L, request);
    assertEquals(theme, response.getTheme());
  }

  private Settings exampleSettings() {
    return new Settings("LIGHT", "DE");
  }

}
