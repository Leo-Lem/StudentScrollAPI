package studentscroll.api.posts.data;

import java.util.Set;
import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("content")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContentPost extends Post {

  @Column(name = "content")
  @NonNull
  private String content;

  public ContentPost(Long posterId, String title, Set<String> tags, String content) {
    super(posterId, title, tags);
    this.content = content;
  }

}
