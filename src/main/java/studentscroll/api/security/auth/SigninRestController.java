package studentscroll.api.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import lombok.val;
import studentscroll.api.security.JSONWebToken;
import studentscroll.api.students.data.Student;

@Tag(name = "Sign in", description = "Signing in to Student Scroll.")
@RestController
@RequestMapping("/signin")
public class SigninRestController {

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private UserDetailsService detailsService;

  @Operation(summary = "Sign in to receive JWT.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Credentials are fine, returning JWT."),
      @ApiResponse(responseCode = "401", description = "Credentials not accepted.", content = @Content) })
  @PostMapping
  public JWTResponse signin(@RequestBody SigninRequest request) throws BadCredentialsException {
    if (authenticate(request.getEmail(), request.getPassword())) {
      val student = (Student) detailsService.loadUserByUsername(request.getEmail());
      return new JWTResponse(
          student.getId(),
          student.getEmail(),
          JSONWebToken.generateFrom(student).toString());
    } else
      throw new BadCredentialsException("");
  }

  private Boolean authenticate(String email, String password) {
    return authManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password))
        .isAuthenticated();
  }

}
