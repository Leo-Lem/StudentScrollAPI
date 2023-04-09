package studentscroll.api.posts.data;

import java.time.LocalDate;
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
  private LocalDate date;

  @Embedded
  @NonNull
  private Location location;

  public EventPost(String title, Set<String> tags, String description, LocalDate date, Location location) {
    super(title, tags);
    this.description = description;
    this.date = date;
    this.location = location;
  }

}
