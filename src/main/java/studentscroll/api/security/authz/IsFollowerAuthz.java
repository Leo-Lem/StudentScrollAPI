package studentscroll.api.security.authz;

import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import lombok.val;
import studentscroll.api.students.data.Student;

@Component
public class IsFollowerAuthz implements AuthorizationManager<RequestAuthorizationContext> {

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    val principalId = ((Student) supplier.get().getPrincipal()).getId();
    val followerId = Long.parseLong(context.getVariables().get("followerId"));

    return new AuthorizationDecision(principalId.equals(followerId));
  }

}