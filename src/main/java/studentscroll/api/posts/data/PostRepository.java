package studentscroll.api.posts.data;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import lombok.NonNull;

public interface PostRepository extends CrudRepository<Post, Long> {

  public Set<Post> findByPosterId(@NonNull Long posterId);

  public Set<Post> findByTitle(@NonNull String title);

  public Set<Post> findDistinctByTagsIn(@NonNull Set<String> tags);

  public Set<Post> findFirst10();

}
