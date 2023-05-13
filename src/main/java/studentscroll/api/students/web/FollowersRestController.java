package studentscroll.api.students.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.students.services.FollowersService;

@Tag(name = "Followers", description = "Everything related to a student's followers.")
@RestController
@RequestMapping("/students/{studentId}/profile")
public class FollowersRestController {

  @Autowired
  private FollowersService service;

  @Operation(summary = "Follow the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Followed the student."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PostMapping("/followers/{followerId}")
  @ResponseStatus(HttpStatus.CREATED)
  public Long follow(
      @PathVariable Long studentId,
      @PathVariable Long followerId) throws EntityNotFoundException {
    return service.follow(studentId, followerId);
  }

  @Operation(summary = "Find all followers.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returning followers."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @GetMapping("/followers")
  public Set<Long> readAllFollowers(
      @PathVariable Long studentId) throws EntityNotFoundException {
    return service.readAllFollowers(studentId);
  }

  @Operation(summary = "Find all follows.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returning follows."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @GetMapping("/follows")
  public Set<Long> readAllFollows(
      @PathVariable Long studentId) throws EntityNotFoundException {
    return service.readAllFollows(studentId);
  }

  @Operation(summary = "Unfollow the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Unfollowed the student."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @DeleteMapping("/followers/{followerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unfollow(
      @PathVariable Long studentId,
      @PathVariable Long followerId) throws EntityNotFoundException {
    service.unfollow(studentId, followerId);
  }

}
