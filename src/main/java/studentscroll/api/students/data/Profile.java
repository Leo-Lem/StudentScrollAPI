package studentscroll.api.students.data;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
public class Profile {

  @Column(name = "name")
  private String name;

  @Column(name = "bio")
  private String bio;

  @Column(name = "icon")
  private String icon;

  // @Column(name = "lastLocation")
  // private Location lastLocation;

}
