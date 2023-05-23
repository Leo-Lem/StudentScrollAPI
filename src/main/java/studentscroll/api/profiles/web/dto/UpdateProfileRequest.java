package studentscroll.api.profiles.web.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import studentscroll.api.shared.StudentLocation;

@Data
@AllArgsConstructor
public class UpdateProfileRequest {

  private final String newName;

  private final String newBio;

  private final String newIcon;

  private final List<String> newInterests;

  private final StudentLocation newLocation;

}
