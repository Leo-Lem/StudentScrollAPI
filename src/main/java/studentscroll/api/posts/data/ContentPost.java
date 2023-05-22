package studentscroll.api.posts.data;

import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@DiscriminatorValue("content")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ContentPost extends Post {

  @Column(name = "content")
  @NonNull
  private String content = "";

  public ContentPost(String title, Set<String> tags, String content) {
    super(title, tags);
    this.content = content;
  }

}
