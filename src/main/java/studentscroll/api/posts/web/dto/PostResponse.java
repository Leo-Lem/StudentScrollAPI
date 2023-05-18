package studentscroll.api.posts.web.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.*;
import studentscroll.api.posts.data.*;
import studentscroll.api.shared.StudentLocation;

@Data
@RequiredArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PostResponse {

  private final Long id;

  @NonNull
  private final Long posterId;

  @NonNull
  private final String title;

  @NonNull
  private final String[] tags;

  // event posts

  private final String description;

  private final LocalDateTime date;

  private final StudentLocation location;

  // content posts

  private final String content;

  public PostResponse(Post post) {
    this.id = post.getId();
    this.posterId = post.getPoster().getId();
    this.title = post.getTitle();
    this.tags = post.getTags().toArray(new String[] {});

    if (post instanceof EventPost) {
      EventPost eventPost = (EventPost) post;
      this.description = eventPost.getDescription();
      this.date = eventPost.getDate();
      this.location = eventPost.getLocation();
      this.content = null;
    } else if (post instanceof ContentPost) {
      ContentPost contentPost = (ContentPost) post;
      this.description = null;
      this.date = null;
      this.location = null;
      this.content = contentPost.getContent();
    } else {
      throw new IllegalArgumentException("Invalid post type");
    }
  }

}
