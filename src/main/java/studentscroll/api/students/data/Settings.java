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

}
