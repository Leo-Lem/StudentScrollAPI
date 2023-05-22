package studentscroll.api.settings;

import lombok.Data;

@Data
public class UpdateSettingsRequest {

  private final String newTheme;
  private final String newLocale;

}
