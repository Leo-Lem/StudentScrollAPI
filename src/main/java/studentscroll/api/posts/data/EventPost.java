package studentscroll.api.posts.data;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
import studentscroll.api.shared.Location;

@Entity
@DiscriminatorValue("event")
@Getter
@NoArgsConstructor
public class EventPost extends Post {

  @Column(name = "description")
  private String description;

  @Column(name = "date")
  private LocalDate date;

  @Embedded
  private Location location;

}
