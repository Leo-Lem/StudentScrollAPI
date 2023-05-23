package studentscroll.api.account.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import studentscroll.api.account.data.Account;
import studentscroll.api.security.JSONWebToken;

@Data
@RequiredArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AccountResponse {

  private final Long id;
  private final String name;
  private final String email;
  private final String token;
  private final String type;

  public AccountResponse(Account student) {
    this(student.getId(), student.getProfile().getName(), student.getEmail(), null, null);
  }

  public AccountResponse(Account student, JSONWebToken token) {
    this(student.getId(), student.getProfile().getName(), student.getEmail(), token.toString(), "Bearer");
  }
}
