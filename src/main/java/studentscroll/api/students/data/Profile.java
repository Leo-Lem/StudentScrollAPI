package studentscroll.api.students.data;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;
import studentscroll.api.shared.Location;

@Embeddable
@Data
@AllArgsConstructor
public class Profile {

  @Column(name = "name")
  private String name;

  @Column(name = "bio")
  private String bio = "";

  @Column(name = "icon")
  private String icon = "STUDENT";

  @Column(name = "interests")
  private Set<String> interests = new HashSet<>();

  @Embedded
  private Location location;

  public Profile(String name) {
    this.name = name;
  }

  public Optional<Location> getLocation() {
    return Optional.ofNullable(location);
  }

  public Profile setLocation(Optional<Location> newLocation) {
    this.location = newLocation.orElse(null);
    return this;
  }

}
