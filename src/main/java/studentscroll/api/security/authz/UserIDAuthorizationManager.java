package studentscroll.api.security.authz;

import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import studentscroll.api.students.data.Student;

@Component
public class UserIDAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    Long principalID = ((Student) supplier.get().getPrincipal()).getId();
    Long requestID = Long.parseLong(context.getVariables().get("userID"));

    return new AuthorizationDecision(principalID.equals(requestID));
  }

}
