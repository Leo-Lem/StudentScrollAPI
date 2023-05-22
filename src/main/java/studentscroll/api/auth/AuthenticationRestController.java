package studentscroll.api.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.media.*;
import lombok.val;
import studentscroll.api.security.JSONWebToken;
import studentscroll.api.security.StudentDetailsService;
import studentscroll.api.auth.dto.*;
import studentscroll.api.students.data.Student;

@Tag(name = "Authentication", description = "Everything related to authentication.")
@RestController
@RequestMapping("/authentication")
public class AuthenticationRestController {

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private StudentDetailsService detailsService;

  @Autowired
  private AuthenticationService service;

  @Operation(summary = "Authenticate for StudentScroll.", description = "If the payload includes a name, tries to create new account. Otherwise, tries to authenticate.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Credentials are fine, returning JWT."),
      @ApiResponse(responseCode = "201", description = "Created a new student, returning JWT."),
      @ApiResponse(responseCode = "401", description = "Credentials are invalid.", content = @Content),
      @ApiResponse(responseCode = "409", description = "Email is already in use.", content = @Content) })
  @PostMapping
  public AuthenticationResponse authenticate(
      @RequestBody AuthenticationRequest request,
      HttpServletResponse response) throws BadCredentialsException {
    Student student;

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

    return new AuthenticationResponse(student, JSONWebToken.generateFrom(student));
  }

  @Operation(summary = "Update your credentials.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated your credentials."),
      @ApiResponse(responseCode = "401", description = "Invalid current password.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PutMapping
  public AuthenticationResponse update(
      @RequestBody UpdateCredentialsRequest request) {
    authenticate(service.readCurrent().getEmail(), request.getCurrentPassword());

    val student = service.update(
        Optional.ofNullable(request.getNewEmail()),
        Optional.ofNullable(request.getNewPassword()));

    return new AuthenticationResponse(student, JSONWebToken.generateFrom(student));
  }

  @Operation(summary = "Delete your account.")
  @ApiResponse(responseCode = "204", description = "Deleted the account.", content = @Content)
  @SecurityRequirement(name = "token")
  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete() {
    service.delete();
  }

  private void authenticate(String email, String password) {
    if (!authManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password))
        .isAuthenticated())
      throw new BadCredentialsException("Invalid email or password.");
  }

}
