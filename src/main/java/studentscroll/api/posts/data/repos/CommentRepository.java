package studentscroll.api.posts.data.repos;

import org.springframework.data.repository.CrudRepository;

import studentscroll.api.posts.data.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
