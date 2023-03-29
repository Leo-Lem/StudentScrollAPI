package studentscroll.api.posts.data;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("event")
@Getter
@Setter
public class EventPost extends Post {

  private String description;
  private LocalDate date;
  // private Location location;

}
