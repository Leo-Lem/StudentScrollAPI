package studentscroll.api.shared;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Location {

  @Column(name = "location_name")
  private String name = null;

  @Column(name = "latitude")
  @NonNull
  private Double latitude;

  @Column(name = "longitude")
  @NonNull
  private Double longitude;

}
