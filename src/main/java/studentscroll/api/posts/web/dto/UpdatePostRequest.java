package studentscroll.api.posts.web.dto;

import java.time.LocalDateTime;

import lombok.*;
import studentscroll.api.shared.StudentLocation;

@Data
public class UpdatePostRequest {

  private final String newTitle;

  private final String[] newTags;

  private final String newDescription;

  private final LocalDateTime newDate;

  private final StudentLocation newLocation;

  private final String newContent;

}
