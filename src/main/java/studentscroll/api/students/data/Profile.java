package studentscroll.api.students.data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.tools.DocumentationTool.Location;

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
  private String icon = "STUDENT";

  @Column(name = "interests")
  private Set<String> interests = new HashSet<>();

  @Column(name = "location")
  private Optional<Location> location;

  public Profile(String name) {
    this.name = name;
  }

}
