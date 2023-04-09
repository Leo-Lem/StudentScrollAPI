package studentscroll.api.posts.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.posts.data.PostRepository;

@Service
public class PostService {

  @Autowired
  private PostRepository repo;

  public void delete(Long postID) throws EntityNotFoundException {
    if (!repo.existsById(postID))
      throw new EntityNotFoundException();

    repo.deleteById(postID);
  }

}