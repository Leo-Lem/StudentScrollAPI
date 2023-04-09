package studentscroll.api.posts.web.dto;

import java.time.LocalDate;

import lombok.*;
import studentscroll.api.shared.Location;

@Data
public class UpdatePostRequest {

  private final String newTitle;

  private final String[] newTags;

  private final String newDescription;

  private final LocalDate newDate;

  private final Location newLocation;

  private final String newContent;

}
