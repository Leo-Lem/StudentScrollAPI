package studentscroll.api.account.web.dto;

import lombok.Data;

@Data
public class UpdateSettingsRequest {

  private final String newTheme;
  private final String newLocale;

}
