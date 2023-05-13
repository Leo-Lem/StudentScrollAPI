package studentscroll.api.students.web.dto;

import lombok.Data;

@Data
public class UpdateSettingsRequest {

  private final String newTheme;
  private final String newLocale;
  private final Boolean newIsLocated;
}
