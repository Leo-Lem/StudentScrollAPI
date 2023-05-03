package studentscroll.api.posts.data;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import studentscroll.api.shared.Location;

@Entity
@DiscriminatorValue("event")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventPost extends Post {

  @Column(name = "description")
  @NonNull
  private String description;

  @Column(name = "date")
  @NonNull
  private LocalDateTime date;

  @Embedded
  @NonNull
  private Location location;

  public EventPost(String title, Set<String> tags, String description, LocalDateTime date, Location location) {
    super(title, tags);
    this.description = description;
    this.date = date;
    this.location = location;
  }

}
