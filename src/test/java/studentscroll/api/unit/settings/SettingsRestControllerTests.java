package studentscroll.api.unit.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lombok.val;
import studentscroll.api.account.data.Settings;
import studentscroll.api.account.services.SettingsService;
import studentscroll.api.account.web.SettingsRestController;
import studentscroll.api.account.web.dto.SettingsResponse;
import studentscroll.api.account.web.dto.UpdateSettingsRequest;

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
  public void givenIsAuthenticated_whenGettingSettings_thenReturnsSettings() {
    val settings = exampleSettings();

    when(service.read())
        .thenReturn(settings);

    assertNotNull(controller.read());
  }

  @Test
  public void givenIsAuthenticated_whenUpdatingSettings_thenReturnsUpdatedSettings() {
    val settings = exampleSettings();
    val request = new UpdateSettingsRequest(
        settings.getTheme(),
        settings.getLocale());

    when(service.update(any(), any()))
        .thenReturn(settings);

    SettingsResponse response = controller.update(request);
    assertEquals(settings.getTheme(), response.getTheme());
    assertEquals(settings.getLocale(), response.getLocale());
  }

  private Settings exampleSettings() {
    return new Settings("LIGHT", "DE");
  }

}
