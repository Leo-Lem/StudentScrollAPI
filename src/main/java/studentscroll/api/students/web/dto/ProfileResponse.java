package studentscroll.api.students.web.dto;

import lombok.*;
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.students.data.Profile;

@Data
@RequiredArgsConstructor
public class ProfileResponse {

  private final String name;
  private final String bio;
  private final String icon;
  private final String[] interests;
  private final StudentLocation location;

  public ProfileResponse(Profile profile) {
    this(
        profile.getName(),
        profile.getBio(),
        profile.getIcon(),
        profile.getInterests().toArray(new String[] {}),
        profile.getLocation().orElse(null));
  }

}
