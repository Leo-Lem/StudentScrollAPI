package studentscroll.api.security.authz;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.posts.data.Post;
import studentscroll.api.posts.data.PostRepository;

@Component
public class IsPosterAuthz implements AuthorizationManager<RequestAuthorizationContext> {

  @Autowired
  private PostRepository repo;

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    val principal = supplier.get().getPrincipal();

    if (!(principal instanceof Student))
      return new AuthorizationDecision(false);

    val requestPostId = Long.parseLong(context.getVariables().get("postId"));
    val posterId = repo.findById(requestPostId).map(Post::getPoster).map(Student::getId);

    return new AuthorizationDecision(posterId.map(id -> id.equals(((Student) principal).getId())).orElse(false));
  }

}
