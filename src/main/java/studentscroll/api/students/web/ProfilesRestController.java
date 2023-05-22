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
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.services.ProfileService;
import studentscroll.api.students.web.dto.*;

@Tag(name = "Profiles", description = "Everything related to a student's profiles.")
@RestController
@RequestMapping("/students/{studentId}/profile")
public class ProfilesRestController {

  @Autowired
  private ProfileService service;

  @Operation(summary = "Find profile of the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the profile."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping
  public ProfileResponse readProfile(
      @PathVariable Long studentId) throws EntityNotFoundException {
    return new ProfileResponse(service.read(studentId));
  }

  @Operation(summary = "Update profile of the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the profile."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PutMapping
  public ProfileResponse updateProfile(
      @PathVariable Long studentId, @RequestBody UpdateProfileRequest request) throws EntityNotFoundException {
    return new ProfileResponse(service.update(
        studentId,
        Optional.ofNullable(request.getNewName()),
        Optional.ofNullable(request.getNewBio()),
        Optional.ofNullable(request.getNewIcon()),
        Optional.ofNullable(request.getNewInterests()).map(Set::of),
        Optional.ofNullable(request.getNewLocation())));
  }

  // @Operation(summary = "Find the students.")
  // @ApiResponse(responseCode = "200", description = "Found the students.")
  // @SecurityRequirement(name = "token")
  // @GetMapping
  // public List<Long> readAll(
  // @RequestParam Double lat,
  // @RequestParam Double lng) throws EntityNotFoundException {
  // return service.readAllNearLocation(new StudentLocation(lat,
  // lng)).stream().map(Student::getId).toList();
  // }

}
