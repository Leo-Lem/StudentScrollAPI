package studentscroll.api.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
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
      @ApiResponse(responseCode = "200", description = "Credentials are fine, returning JWT.", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponse.class)) }),
      @ApiResponse(responseCode = "401", description = "Credentials not accepted.", content = @Content) })
  @PostMapping
  public ResponseEntity<?> signin(@Validated @RequestBody SigninRequest request) {
    try {
      if (authenticate(request.getEmail(), request.getPassword())) {
        val student = (Student) detailsService.loadUserByUsername(request.getEmail());
        val jwt = JSONWebToken.generateFrom(student);

        return ResponseEntity.ok(new JWTResponse(student.getId(), student.getEmail(), jwt.toString()));
      } else
        throw new BadCredentialsException("");
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(401).build();
    }
  }

  private Boolean authenticate(String email, String password) {
    return authManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password))
        .isAuthenticated();
  }

}
