package studentscroll.api.posts.services;

import java.util.*;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.posts.data.*;

@Service
public class ContentPostService extends PostService {

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