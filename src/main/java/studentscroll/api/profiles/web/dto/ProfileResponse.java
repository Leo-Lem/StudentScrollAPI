package studentscroll.api.profiles.web.dto;

import lombok.*;
import studentscroll.api.profiles.data.Profile;
import studentscroll.api.shared.StudentLocation;

@Data
@RequiredArgsConstructor
public class ProfileResponse {

  private final Long studentId;
  private final String name;
  private final String bio;
  private final String icon;
  private final String[] interests;
  private final StudentLocation location;

  public ProfileResponse(Profile profile) {
    this(
        profile.getId(),
        profile.getName(),
        profile.getBio(),
        profile.getIcon(),
        profile.getInterests().toArray(new String[] {}),
        profile.getLocation().orElse(null));
  }

}
