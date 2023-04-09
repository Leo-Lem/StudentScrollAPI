package studentscroll.api.students.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import lombok.*;
import studentscroll.api.students.services.*;
import studentscroll.api.students.web.dto.*;

@Tag(name = "Students", description = "Everything related to students.")
@RestController
@RequestMapping("/students")
public class StudentsRestController {

  @Autowired
  private StudentService studentService;

  @Autowired
  private ProfileService profileService;

  // @Autowired
  // private SettingsService settingsService;

  @Operation(summary = "Create a new student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created the student.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))),
      @ApiResponse(responseCode = "409", description = "Email is already in use.", content = @Content) })
  @PostMapping
  public ResponseEntity<?> registerStudent(@RequestBody CreateStudentRequest request) {
    try {
      val student = studentService.create(request.getName(), request.getEmail(), request.getPassword());
      val response = new StudentResponse(student);
      return ResponseEntity.created(new URI("/students/" + response.getId())).body(response);
    } catch (EntityExistsException e) {
      return ResponseEntity
          .status(409)
          .body("Email is already in use!");
    } catch (URISyntaxException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "Find the profile of the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the profile.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping("/{studentId}/profile")
  public ResponseEntity<?> readProfile(@PathVariable Long studentId) {
    try {
      return ResponseEntity.ok().body(new ProfileResponse(profileService.read(studentId)));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Update the profile of the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the profile.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "student themself")
  @PutMapping("/{studentId}/profile")
  public ResponseEntity<?> updateProfile(@PathVariable Long studentId, @RequestBody UpdateProfileRequest request) {
    try {
      val profile = profileService.update(
          studentId,
          Optional.ofNullable(request.getNewName()),
          Optional.ofNullable(request.getNewBio()),
          Optional.ofNullable(request.getNewIcon()),
          Optional.ofNullable(request.getNewInterests()).map(Set::of),
          Optional.ofNullable(request.getNewLocation()));

      return ResponseEntity.ok().body(new ProfileResponse(profile));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

}
