package studentscroll.api.security.authz;

import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import lombok.val;
import studentscroll.api.account.data.Student;

@Component
public class IsStudentAuthz implements AuthorizationManager<RequestAuthorizationContext> {

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    val principal = supplier.get().getPrincipal();

    if (!(principal instanceof Student))
      return new AuthorizationDecision(false);

    val requestID = Long.parseLong(context.getVariables().get("studentId"));

    return new AuthorizationDecision(((Student) principal).getId().equals(requestID));
  }

}
