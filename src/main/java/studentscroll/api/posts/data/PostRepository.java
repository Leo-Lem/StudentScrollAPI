package studentscroll.api.posts.data;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
  Page<Post> findByPosterIdIn(List<Long> posterIds, Pageable page);

  Page<Post> findByPosterIdNotIn(List<Long> posterIds, Pageable page);

  Page<Post> findByTags(String tag, Pageable page);

  Page<ContentPost> findByContentLike(String pattern, Pageable page);
}
