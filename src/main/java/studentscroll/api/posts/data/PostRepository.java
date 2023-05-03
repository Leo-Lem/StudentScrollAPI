package studentscroll.api.posts.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
  Page<Post> findByPosterId(Long id, Pageable page);

  Page<Post> findByTags(String tag, Pageable page);

  Page<ContentPost> findByContentLike(String pattern, Pageable page);
}
