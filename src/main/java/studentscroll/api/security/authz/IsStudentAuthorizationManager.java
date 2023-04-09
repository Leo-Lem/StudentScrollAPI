package studentscroll.api.security.authz;

import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import lombok.val;
import studentscroll.api.students.data.*;

@Component
public class IsStudentAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    val principalID = ((Student) supplier.get().getPrincipal()).getId();
    val requestID = Long.parseLong(context.getVariables().get("studentID"));

    return new AuthorizationDecision(principalID.equals(requestID));
  }

}
