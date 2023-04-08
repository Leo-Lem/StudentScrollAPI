package studentscroll.api.students.web.dto;

import lombok.Data;
import studentscroll.api.shared.Location;

@Data
public class ProfileResponse {

  private String name;
  private String bio;
  private String icon;
  private String[] interests;
  private Location location;

}
