package studentscroll.api.posts.data;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
  Page<Post> findByPosterIdIn(List<Long> posterIds, Pageable page);

  Page<Post> findByPosterIdNotIn(List<Long> posterIds, Pageable page);

  Page<Post> findByTitleLikeIgnoreCase(String title, Pageable page);

  Page<Post> findByTagsInIgnoreCase(Set<String> tags, Pageable page);

  Page<ContentPost> findByContentLike(String pattern, Pageable page);

}
