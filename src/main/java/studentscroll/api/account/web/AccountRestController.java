package studentscroll.api.account.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.account.data.Account;
import studentscroll.api.account.services.AccountService;
import studentscroll.api.account.web.dto.AuthenticationRequest;
import studentscroll.api.account.web.dto.AccountResponse;
import studentscroll.api.account.web.dto.UpdateCredentialsRequest;
import studentscroll.api.security.JSONWebToken;
import studentscroll.api.security.StudentDetailsService;
import studentscroll.api.shared.NotAuthenticatedException;

@Tag(name = "Account", description = "Everything related to your account and authentication.")
@RestController
@RequestMapping("/account")
public class AccountRestController {

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private StudentDetailsService detailsService;

  @Autowired
  private AccountService service;

  @Operation(summary = "Authenticate to StudentScroll.", description = "If the payload includes a name, tries to create new account. Otherwise, tries to authenticate.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Credentials are fine, returning JWT."),
      @ApiResponse(responseCode = "201", description = "Created a new student, returning JWT."),
      @ApiResponse(responseCode = "401", description = "Credentials are invalid.", content = @Content),
      @ApiResponse(responseCode = "409", description = "Email is already in use.", content = @Content) })
  @PostMapping
  public AccountResponse authenticate(
      @RequestBody AuthenticationRequest request,
      HttpServletResponse response) throws BadCredentialsException {
    Account student;

    if (request.getName() == null) {
      authenticate(request.getEmail(), request.getPassword());
      student = detailsService.loadUserByUsername(request.getEmail());
    } else {
      student = service.create(
          request.getName(),
          request.getEmail(),
          request.getPassword());

      response.setStatus(201);
      response.setHeader("Location", "/students/" + student.getId());
    }

    return new AccountResponse(student, JSONWebToken.generateFrom(student));
  }

  @Operation(summary = "Update your credentials.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated your credentials."),
      @ApiResponse(responseCode = "401", description = "Invalid email or password.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PutMapping
  public AccountResponse update(
      @RequestBody UpdateCredentialsRequest request) throws NotAuthenticatedException {
    authenticate(request.getCurrentEmail(), request.getCurrentPassword());

    val student = service.update(
        getCurrentStudent(),
        Optional.ofNullable(request.getNewEmail()),
        Optional.ofNullable(request.getNewPassword()));

    return new AccountResponse(student, JSONWebToken.generateFrom(student));
  }

  @Operation(summary = "Delete your account.")
  @ApiResponse(responseCode = "204", description = "Deleted the account.", content = @Content)
  @SecurityRequirement(name = "token")
  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete() throws NotAuthenticatedException {
    service.delete(getCurrentStudent().getId());
  }

  private void authenticate(String email, String password) {
    if (!authManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password))
        .isAuthenticated())
      throw new BadCredentialsException("Invalid email or password.");
  }

  private Account getCurrentStudent() throws NotAuthenticatedException {
    val student = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (student == null)
      throw new NotAuthenticatedException("You are not logged in.");

    return student;
  }

}
