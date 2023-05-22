package studentscroll.api.students.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.students.services.FollowersService;

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
  public Set<Long> readAllFollowers(
      @PathVariable Long studentId) throws EntityNotFoundException {
    return service.readAllFollowers(studentId);
  }

  @Operation(summary = "Find student's follows.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Returning follows."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping("/follows")
  @ResponseStatus(HttpStatus.OK)
  public Set<Long> readAllFollows(
      @PathVariable Long studentId) throws EntityNotFoundException {
    return service.readAllFollows(studentId);
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
  public Long follow(
      @PathVariable Long studentId) throws EntityNotFoundException, EntityExistsException, IllegalArgumentException {
    return service.follow(studentId, getCurrentStudent().getId());
  }

  @Operation(summary = "Unfollow the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Unfollowed the student."),
      @ApiResponse(responseCode = "404", description = "Unfollower is not following or student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @DeleteMapping("/followers")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unfollow(
      @PathVariable Long studentId) throws EntityNotFoundException {
    service.unfollow(studentId, getCurrentStudent().getId());
  }

  private Student getCurrentStudent() throws IllegalStateException {
    val student = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (student == null)
      throw new IllegalStateException("Not authenticated.");

    return student;
  }

}
