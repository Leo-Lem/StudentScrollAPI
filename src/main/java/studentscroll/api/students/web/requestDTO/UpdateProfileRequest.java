package studentscroll.api.students.web.requestDTO;

import java.util.*;
import lombok.Data;
import studentscroll.api.shared.Location;

@Data
public class UpdateProfileRequest {

  private Optional<String> name;
  private Optional<String> bio;
  private Optional<String> icon;
  private Optional<String[]> interests;
  private Optional<Location> location;

}
