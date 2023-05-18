package studentscroll.api.security.authz;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import lombok.val;
import studentscroll.api.chats.data.MessageRepository;
import studentscroll.api.students.data.Student;

@Component
public class IsSenderAuthz implements AuthorizationManager<RequestAuthorizationContext> {

  @Autowired
  private MessageRepository repo;

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    val principalId = ((Student) supplier.get().getPrincipal()).getId();
    val messageId = Long.parseLong(context.getVariables().get("messageId"));

    val message = repo.findById(messageId);

    return new AuthorizationDecision(
        message.isPresent()
            && message.get().getSender().getId().equals(principalId));
  }

}