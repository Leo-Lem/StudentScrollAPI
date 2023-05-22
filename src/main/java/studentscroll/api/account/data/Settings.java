package studentscroll.api.account.data;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Settings {

  @Column(name = "theme")
  private String theme = "system";

  @Column(name = "locale")
  private String locale = "system";

}
