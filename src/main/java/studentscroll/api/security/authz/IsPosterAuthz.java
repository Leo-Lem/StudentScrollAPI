package studentscroll.api.security.authz;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import lombok.val;
import studentscroll.api.posts.data.*;
import studentscroll.api.students.data.*;

@Component
public class IsPosterAuthz implements AuthorizationManager<RequestAuthorizationContext> {

  @Autowired
  private PostRepository repo;

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    val principalId = ((Student) supplier.get().getPrincipal()).getId();
    val requestPostId = Long.parseLong(context.getVariables().get("postId"));
    val posterId = repo.findById(requestPostId).map(Post::getPoster).map(Student::getId);

    return new AuthorizationDecision(posterId.map(id -> id.equals(principalId)).orElse(false));
  }

}
