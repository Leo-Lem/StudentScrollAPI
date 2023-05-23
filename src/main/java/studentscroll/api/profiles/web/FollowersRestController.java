package studentscroll.api.profiles.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.account.data.Account;
import studentscroll.api.profiles.services.FollowersService;
import studentscroll.api.profiles.web.dto.ProfileResponse;
import studentscroll.api.shared.NotAuthenticatedException;

@Tag(name = "Followers", description = "Everything related to a student's followers.")
@RestController
@RequestMapping("/students/{studentId}")
public class FollowersRestController {

  @Autowired
  private FollowersService service;

  @Operation(summary = "Find student's followers.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returning followers."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping("/followers")
  @ResponseStatus(HttpStatus.OK)
  public List<ProfileResponse> readAllFollowers(@PathVariable Long studentId) throws EntityNotFoundException {
    return service.readFollowers(studentId).stream().map(ProfileResponse::new).toList();
  }

  @Operation(summary = "Find student's follows.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returning follows."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping("/follows")
  @ResponseStatus(HttpStatus.OK)
  public List<ProfileResponse> readAllFollows(@PathVariable Long studentId) throws EntityNotFoundException {
    return service.readFollows(studentId).stream().map(ProfileResponse::new).toList();
  }

  @Operation(summary = "Follow the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Followed the student."),
      @ApiResponse(responseCode = "400", description = "Student cannot follow themselves.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content),
      @ApiResponse(responseCode = "409", description = "Student is already followed.", content = @Content)
  })
  @SecurityRequirement(name = "token")
  @PostMapping("/followers")
  @ResponseStatus(HttpStatus.CREATED)
  public ProfileResponse follow(@PathVariable Long studentId)
      throws EntityNotFoundException, EntityExistsException, IllegalArgumentException, NotAuthenticatedException {
    return new ProfileResponse(service.follow(getCurrentStudent().getProfile(), studentId));
  }

  @Operation(summary = "Unfollow the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Unfollowed the student."),
      @ApiResponse(responseCode = "404", description = "Unfollower is not following or student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @DeleteMapping("/followers")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unfollow(@PathVariable Long studentId) throws EntityNotFoundException, NotAuthenticatedException {
    service.unfollow(getCurrentStudent().getProfile(), studentId);
  }

  private Account getCurrentStudent() throws NotAuthenticatedException {
    val student = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (student == null)
      throw new NotAuthenticatedException("You are not logged in.");

    return student;
  }

}
