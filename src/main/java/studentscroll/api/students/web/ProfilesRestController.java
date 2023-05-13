package studentscroll.api.students.web;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.students.services.ProfileService;
import studentscroll.api.students.web.dto.*;

@Tag(name = "Profiles", description = "Everything related to a student's profiles.")
@RestController
@RequestMapping("/students/{studentId}/profile")
public class ProfilesRestController {

  @Autowired
  private ProfileService profileService;

  @Operation(summary = "Find profile of the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the profile."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping
  public ProfileResponse readProfile(
      @PathVariable Long studentId) throws EntityNotFoundException {
    return new ProfileResponse(profileService.read(studentId));
  }

  @Operation(summary = "Update profile of the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the profile."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PutMapping
  public ProfileResponse updateProfile(
      @PathVariable Long studentId, @RequestBody UpdateProfileRequest request) throws EntityNotFoundException {
    return new ProfileResponse(profileService.update(
        studentId,
        Optional.ofNullable(request.getNewName()),
        Optional.ofNullable(request.getNewBio()),
        Optional.ofNullable(request.getNewIcon()),
        Optional.ofNullable(request.getNewInterests()).map(Set::of),
        Optional.ofNullable(request.getNewLocation())));
  }
}
