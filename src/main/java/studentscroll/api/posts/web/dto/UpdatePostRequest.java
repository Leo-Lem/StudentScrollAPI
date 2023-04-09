package studentscroll.api.posts.web.dto;

import java.time.LocalDate;

import org.hibernate.TypeMismatchException;

import lombok.*;
import studentscroll.api.posts.data.ContentPost;
import studentscroll.api.posts.data.EventPost;
import studentscroll.api.posts.data.Post;
import studentscroll.api.shared.Location;

@Data
public class UpdatePostRequest {

  private final String newTitle;

  private final String[] newTags;

  private final String newDescription;

  private final LocalDate newDate;

  private final Location newLocation;

  private final String newContent;

  public Class<? extends Post> getType() throws TypeMismatchException {
    if (newDescription != null && newDate != null && newLocation == null)
      return EventPost.class;
    else if (newContent != null)
      return ContentPost.class;
    else
      throw new TypeMismatchException("Unknown post type.");
  }

}
