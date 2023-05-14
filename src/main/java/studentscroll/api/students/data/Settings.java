package studentscroll.api.students.data;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

  @Column(name = "theme")
  private String theme = "system";

  @Column(name = "locale")
  private String locale = "system";

  @Column(name = "isLocated")
  private Boolean isLocated = false;

  // @Column(name = "isNotified")
  // private Boolean isNotified = false;

  // @Column(name = "privacy")
  // private String privacy = "VISIBLE";

  // @OneToMany
  // @JoinColumn
  // private Set<Student> blocked;

}
