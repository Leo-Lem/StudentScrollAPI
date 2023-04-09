package studentscroll.api.posts.services;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import studentscroll.api.posts.data.*;
import studentscroll.api.shared.Location;

@Service
public class PostService {

  @Autowired
  protected PostRepository repo;

  public EventPost create(
      @NonNull Long posterId,
      @NonNull String title,
      @NonNull Set<String> tags,
      @NonNull String description,
      @NonNull LocalDate date,
      @NonNull Location location) {
    return repo.save(new EventPost(posterId, title, tags, description, date, location));
  }

  public ContentPost create(
      @NonNull Long posterId,
      @NonNull String title,
      @NonNull Set<String> tags,
      @NonNull String content) {
    return repo.save(new ContentPost(posterId, title, tags, content));
  }

  public Post read(
      @NonNull Long postID) throws EntityNotFoundException {
    return repo.findById(postID).orElseThrow(() -> new EntityNotFoundException());
  }

  public Post update(
      @NonNull Long postID,
      @NonNull Optional<String> newTitle,
      @NonNull Optional<Set<String>> newTags,
      @NonNull Optional<String> newDescription,
      @NonNull Optional<LocalDate> newDate,
      @NonNull Optional<Location> newLocation,
      @NonNull Optional<String> newContent) throws EntityNotFoundException {
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

  public void delete(
      @NonNull Long postID) throws EntityNotFoundException {
    if (!repo.existsById(postID))
      throw new EntityNotFoundException();

    repo.deleteById(postID);
  }

}