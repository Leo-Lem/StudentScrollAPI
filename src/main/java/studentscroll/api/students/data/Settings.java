package studentscroll.api.students.data;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

  @Column(name = "isNotified")
  private Boolean isNotified = false;

  @Column(name = "isLocated")
  private Boolean isLocated = false;

  @Column(name = "theme")
  private String theme = "DARK";

  @Column(name = "locale")
  private String locale = "EN";

  @Column(name = "privacy")
  private Privacy privacy = Privacy.VISIBLE;

  // @OneToMany
  // @JoinColumn
  // private Set<Student> blocked;

}
