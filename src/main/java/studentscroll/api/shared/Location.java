package studentscroll.api.shared;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Location {

  @Column(name = "description")
  private String description;

  @Column(name = "latitude")
  private Long latitude;

  @Column(name = "longitude")
  private Long longitude;

}
