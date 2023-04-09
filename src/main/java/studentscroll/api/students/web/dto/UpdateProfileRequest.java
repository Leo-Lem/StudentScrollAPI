package studentscroll.api.students.web.dto;

import lombok.*;
import studentscroll.api.shared.Location;

@Data
public class UpdateProfileRequest {

  private final String name;
  private final String bio;
  private final String icon;
  private final String[] interests;
  private final Location location;

}
