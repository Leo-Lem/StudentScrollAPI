package studentscroll.api.shared;

import jakarta.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class Location {

  @Column(name = "description")
  private final String description;

  @Column(name = "latitude")
  private final Double latitude;

  @Column(name = "longitude")
  private final Double longitude;

}
