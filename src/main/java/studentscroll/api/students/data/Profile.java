package studentscroll.api.students.data;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

  @Column(name = "name")
  private String name = "<Anonymous>";

  @Column(name = "bio")
  private String bio = "";

  @Column(name = "icon")
  private String icon = "student";

  // @Column(name = "lastLocation")
  // private Location lastLocation;

  public Profile(String name) {
    this.name = name;
  }

}
