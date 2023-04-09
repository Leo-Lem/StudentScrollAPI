package studentscroll.api.posts.services;

import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.posts.data.EventPost;
import studentscroll.api.shared.Location;

@Service
public class EventPostService extends PostService {

  public EventPost create(
      Long posterId, String title, String description, LocalDate date, Location location, Set<String> tags) {
    return repo.save(new EventPost(posterId, title, tags, description, date, location));
  }

  public EventPost update(
      Long postID,
      Optional<String> newTitle,
      Optional<String> newDescription,
      Optional<LocalDate> newDate,
      Optional<Location> newLocation,
      Optional<Set<String>> newTags) throws EntityNotFoundException {
    EventPost post;

    if ((post = (EventPost) repo.findById(postID).orElse(null)) == null)
      throw new EntityNotFoundException();

    newTitle.ifPresent(unwrapped -> post.setTitle(unwrapped));
    newDescription.ifPresent(unwrapped -> post.setDescription(unwrapped));
    newDate.ifPresent(unwrapped -> post.setDate(unwrapped));
    newLocation.ifPresent(unwrapped -> post.setLocation(unwrapped));
    newTags.ifPresent(unwrapped -> post.setTags(unwrapped));

    return repo.save(post);
  }

}