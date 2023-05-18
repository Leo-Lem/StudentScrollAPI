package studentscroll.api.security.authz;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import lombok.val;
import studentscroll.api.chats.data.ChatRepository;
import studentscroll.api.students.data.Student;

@Component
public class IsParticipantAuthz implements AuthorizationManager<RequestAuthorizationContext> {

  @Autowired
  private ChatRepository repo;

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
    val principalId = ((Student) supplier.get().getPrincipal()).getId();
    val requestChatId = Long.parseLong(context.getVariables().get("chatId"));

    val chat = repo.findById(requestChatId);

    return new AuthorizationDecision(
        chat.isPresent()
            && chat.get().getParticipants().stream().anyMatch(participant -> participant.getId().equals(principalId)));
  }

}