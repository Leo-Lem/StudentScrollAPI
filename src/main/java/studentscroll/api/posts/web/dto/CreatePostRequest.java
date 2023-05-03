package studentscroll.api.posts.web.dto;

import java.time.LocalDateTime;

import lombok.*;
import studentscroll.api.posts.data.*;
import studentscroll.api.shared.Location;

@Data
public class CreatePostRequest {

  @NonNull
  private final Long posterId;

  @NonNull
  private final String title;

  @NonNull
  private final String[] tags;

  // event posts
  private final String description;

  private final LocalDateTime date;

  private final Location location;

  // content posts
  private final String content;

  public Class<? extends Post> getType() {
    if (description != null && date != null && location != null)
      return EventPost.class;
    else if (content != null)
      return ContentPost.class;
    else
      return null;
  }

}
