package studentscroll.api.posts.data.repos;

import org.springframework.data.repository.CrudRepository;

import studentscroll.api.posts.data.Post;

public interface PostRepository extends CrudRepository<Post, Long> {
}
