package studentscroll.api.students.data;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
public class Settings {

  @Column(name = "isNotified")
  private Boolean isNotified;

  @Column(name = "theme")
  private Theme theme;

  @Column(name = "locale")
  private Locale locale;

  // @OneToMany
  // @JoinColumn
  // private Set<Student> blocked;

}
