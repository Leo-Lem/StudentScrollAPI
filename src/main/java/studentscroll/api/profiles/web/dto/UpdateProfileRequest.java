package studentscroll.api.profiles.web.dto;

import lombok.*;
import studentscroll.api.shared.StudentLocation;

@Data
public class UpdateProfileRequest {

  private final String newName;
  private final String newBio;
  private final String newIcon;
  private final String[] newInterests;
  private final StudentLocation newLocation;

}
