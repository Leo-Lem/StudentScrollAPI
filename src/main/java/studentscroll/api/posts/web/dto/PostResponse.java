package studentscroll.api.posts.web.dto;

import java.time.LocalDate;

import lombok.*;
import studentscroll.api.posts.data.*;
import studentscroll.api.shared.Location;

@Data
@RequiredArgsConstructor
public class PostResponse {

  private final Long id;

  @NonNull
  private final Long posterId;

  @NonNull
  private final String title;

  @NonNull
  private final String[] tags;

  private final String description;

  private final LocalDate date;

  private final Location location;

  private final String content;

  public PostResponse(EventPost post) {
    this(post.getId(),
        post.getPosterId(), post.getTitle(), post.getTags().toArray(new String[] {}),
        post.getDescription(), post.getDate(), post.getLocation(), null);
  }

  public PostResponse(ContentPost post) {
    this(post.getId(),
        post.getPosterId(), post.getTitle(), post.getTags().toArray(new String[] {}),
        null, null, null, post.getContent());
  }

}
