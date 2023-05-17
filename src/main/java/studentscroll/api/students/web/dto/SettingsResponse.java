package studentscroll.api.students.web.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import studentscroll.api.students.data.Settings;

@Data
@RequiredArgsConstructor
public class SettingsResponse {

  private final String theme;
  private final String locale;

  public SettingsResponse(Settings settings) {
    this(
        settings.getTheme(),
        settings.getLocale());
  }
}
