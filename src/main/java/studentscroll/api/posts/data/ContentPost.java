package studentscroll.api.posts.data;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("content")
@Getter
@NoArgsConstructor
public class ContentPost extends Post {

  @Column(name = "content")
  private String content;

}
