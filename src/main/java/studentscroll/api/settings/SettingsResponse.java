package studentscroll.api.settings;

import lombok.Data;
import lombok.RequiredArgsConstructor;

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
