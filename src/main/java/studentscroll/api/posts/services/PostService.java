package studentscroll.api.posts.services;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.posts.data.*;
import studentscroll.api.shared.Location;

@Service
public class PostService {

  @Autowired
  protected PostRepository repo;

  public EventPost create(
      Long posterId, String title, Set<String> tags,
      String description, LocalDate date, Location location) {
    return repo.save(new EventPost(posterId, title, tags, description, date, location));
  }

  public ContentPost create(Long posterId, String title, Set<String> tags, String content) {
    return repo.save(new ContentPost(posterId, title, tags, content));
  }

  public Post read(Long postID) throws EntityNotFoundException {
    return repo.findById(postID).orElseThrow(() -> new EntityNotFoundException());
  }

  public Post update(
      Long postID,
      Optional<String> newTitle,
      Optional<Set<String>> newTags,
      Optional<String> newDescription,
      Optional<LocalDate> newDate,
      Optional<Location> newLocation,
      Optional<String> newContent) throws EntityNotFoundException {
    try {
      final Post post = read(postID);

      newTitle.ifPresent(unwrapped -> post.setTitle(unwrapped));
      newTags.ifPresent(unwrapped -> post.setTags(unwrapped));

      if (post instanceof EventPost) {
        EventPost eventPost = (EventPost) post;
        newDescription.ifPresent(unwrapped -> eventPost.setDescription(unwrapped));
        newDate.ifPresent(unwrapped -> eventPost.setDate(unwrapped));
        newLocation.ifPresent(unwrapped -> eventPost.setLocation(unwrapped));
        return repo.save(eventPost);
      } else if (post instanceof ContentPost) {
        ContentPost contentPost = (ContentPost) post;
        newContent.ifPresent(unwrapped -> contentPost.setContent(unwrapped));
        return repo.save(contentPost);
      } else
        return repo.save(post);
    } catch (ClassCastException e) {
      throw new EntityNotFoundException("Updated post is of wrong type.");
    }
  }

  public void delete(Long postID) throws EntityNotFoundException {
    if (!repo.existsById(postID))
      throw new EntityNotFoundException();

    repo.deleteById(postID);
  }

}