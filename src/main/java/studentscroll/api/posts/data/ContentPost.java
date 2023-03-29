package studentscroll.api.posts.data;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("content")
@Getter
@Setter
public class ContentPost extends Post {

  private String content;

}
