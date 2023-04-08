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

  @Column(name = "theme")
  private Theme theme = Theme.DARK;

  @Column(name = "locale")
  private Locale locale = Locale.ENGLISH;

  // @OneToMany
  // @JoinColumn
  // private Set<Student> blocked;

}
