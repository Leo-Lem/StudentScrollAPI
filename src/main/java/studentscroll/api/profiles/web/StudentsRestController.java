package studentscroll.api.profiles.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import studentscroll.api.profiles.data.Profile;
import studentscroll.api.profiles.services.FollowersService;
import studentscroll.api.profiles.services.ProfileService;
import studentscroll.api.profiles.web.dto.ProfileResponse;
import studentscroll.api.profiles.web.dto.UpdateProfileRequest;
import studentscroll.api.shared.NotAuthenticatedException;
import studentscroll.api.shared.StudentLocation;

@Tag(name = "Students", description = "Everything related to students.")
@RestController
@RequestMapping("/students")
public class StudentsRestController {

  @Autowired
  private ProfileService service;

  @Autowired
  private FollowersService followersService;

  @Operation(summary = "Find student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the profile."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping("/{studentId}")
  public ProfileResponse read(
      @PathVariable Long studentId) throws EntityNotFoundException {
    return new ProfileResponse(service.read(studentId));
  }

  @Operation(summary = "Find students matching query.")
  @ApiResponse(responseCode = "200", description = "Found the students.")
  @SecurityRequirement(name = "token")
  @GetMapping
  public List<ProfileResponse> readAll(
      @RequestParam Optional<String> name,
      @RequestParam Optional<List<String>> interests,
      @RequestParam Optional<Double> lat,
      @RequestParam Optional<Double> lng) {
    List<Profile> profiles;

    if (name.isPresent())
      profiles = service.readByName(name.get());
    else if (interests.isPresent())
      profiles = service.readByInterests(interests.get());
    else if (lat.isPresent() && lng.isPresent())
      profiles = service.readAllNearLocation(new StudentLocation(lat.get(), lng.get()));
    else
      profiles = service.readAll();

    return profiles.stream().map(ProfileResponse::new).toList();
  }

  @Operation(summary = "Update your profile.")
  @ApiResponse(responseCode = "200", description = "Updated the profile.")
  @SecurityRequirement(name = "token")
  @PutMapping
  public ProfileResponse updateProfile(@RequestBody UpdateProfileRequest request) throws NotAuthenticatedException {
    return new ProfileResponse(service.update(
        getCurrentStudent().getProfile(),
        Optional.ofNullable(request.getNewName()),
        Optional.ofNullable(request.getNewBio()),
        Optional.ofNullable(request.getNewIcon()),
        Optional.ofNullable(request.getNewInterests()),
        Optional.ofNullable(request.getNewLocation())));
  }

  @Operation(summary = "Follow the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Followed the student."),
      @ApiResponse(responseCode = "400", description = "Student cannot follow themselves.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content),
      @ApiResponse(responseCode = "409", description = "Student is already followed.", content = @Content)
  })
  @SecurityRequirement(name = "token")
  @PostMapping("/{studentId}/followers")
  @ResponseStatus(HttpStatus.CREATED)
  public ProfileResponse follow(@PathVariable Long studentId)
      throws EntityNotFoundException, EntityExistsException, IllegalArgumentException, NotAuthenticatedException {
    return new ProfileResponse(followersService.follow(getCurrentStudent().getProfile(), studentId));
  }

  @Operation(summary = "Unfollow the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Unfollowed the student."),
      @ApiResponse(responseCode = "404", description = "Unfollower is not following or student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @DeleteMapping("/{studentId}/followers")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unfollow(@PathVariable Long studentId) throws EntityNotFoundException, NotAuthenticatedException {
    followersService.unfollow(getCurrentStudent().getProfile(), studentId);
  }

  private Account getCurrentStudent() throws NotAuthenticatedException {
    val student = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (student == null)
      throw new NotAuthenticatedException("You are not logged in.");

    return student;
  }

}
