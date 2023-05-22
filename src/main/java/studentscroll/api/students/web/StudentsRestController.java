package studentscroll.api.students.web;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.shared.NotAuthenticatedException;
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.students.services.ProfileService;
import studentscroll.api.students.web.dto.ProfileResponse;
import studentscroll.api.students.web.dto.UpdateProfileRequest;

@Tag(name = "Students", description = "Everything related to students.")
@RestController
@RequestMapping("/students")
public class StudentsRestController {

  @Autowired
  private ProfileService service;

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

  @Operation(summary = "Find students.")
  @ApiResponse(responseCode = "200", description = "Found the students.")
  @SecurityRequirement(name = "token")
  @GetMapping
  public List<Long> readAll(
      @RequestParam Double lat,
      @RequestParam Double lng) {
    return service.readAllNearLocation(new StudentLocation(lat, lng)).stream().map(Student::getId).toList();
  }

  @Operation(summary = "Update your profile.")
  @ApiResponse(responseCode = "200", description = "Updated the profile.")
  @SecurityRequirement(name = "token")
  @PutMapping
  public ProfileResponse update(
      @RequestBody UpdateProfileRequest request) throws NotAuthenticatedException {
    return new ProfileResponse(service.update(
        getCurrentStudent(),
        Optional.ofNullable(request.getNewName()),
        Optional.ofNullable(request.getNewBio()),
        Optional.ofNullable(request.getNewIcon()),
        Optional.ofNullable(request.getNewInterests()).map(Set::of),
        Optional.ofNullable(request.getNewLocation())));
  }

  private Student getCurrentStudent() throws NotAuthenticatedException {
    val student = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (student == null)
      throw new NotAuthenticatedException("You are not logged in.");

    return student;
  }

}
