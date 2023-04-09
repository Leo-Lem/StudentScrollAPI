package studentscroll.api.posts.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.posts.data.*;

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

}