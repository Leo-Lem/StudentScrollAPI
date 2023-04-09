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

  public Post read(Long postID) throws EntityNotFoundException {
    return repo.findById(postID).orElseThrow(() -> new EntityNotFoundException());
  }

  public void delete(Long postID) throws EntityNotFoundException {
    if (!repo.existsById(postID))
      throw new EntityNotFoundException();

    repo.deleteById(postID);
  }

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

  public ContentPost create(Long posterId, String title, String content, Set<String> tags) {
    return repo.save(new ContentPost(posterId, title, tags, content));
  }

  public ContentPost update(
      Long postID,
      Optional<String> newTitle,
      Optional<String> newContent,
      Optional<Set<String>> newTags) throws EntityNotFoundException {
    ContentPost post;

    if ((post = (ContentPost) repo.findById(postID).orElse(null)) == null)
      throw new EntityNotFoundException();

    newTitle.ifPresent(unwrapped -> post.setTitle(unwrapped));
    newContent.ifPresent(unwrapped -> post.setContent(unwrapped));
    newTags.ifPresent(unwrapped -> post.setTags(unwrapped));

    return repo.save(post);
  }

}