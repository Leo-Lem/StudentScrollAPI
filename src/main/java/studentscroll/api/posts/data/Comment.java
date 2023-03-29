package studentscroll.api.posts.data;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "comment")
@Data
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

}
