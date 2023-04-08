package studentscroll.api.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.val;
import studentscroll.api.security.JSONWebToken;
import studentscroll.api.students.data.Student;

@RestController
@RequestMapping("/signin")
public class SigninRestController {

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private UserDetailsService detailsService;

  @PostMapping
  public ResponseEntity<?> signin(@Validated @RequestBody SigninRequest request) {
    try {
      if (authenticate(request.getEmail(), request.getPassword())) {
        val student = (Student) detailsService.loadUserByUsername(request.getEmail());
        val jwt = JSONWebToken.generateFrom(student);

        return ResponseEntity.ok(JWTResponse.builder()
            .id(student.getId())
            .email(student.getEmail())
            .token(jwt.toString())
            .build());
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
