package studentscroll.api.students.web;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletResponse;
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
      @ApiResponse(responseCode = "201", description = "Created the student."),
      @ApiResponse(responseCode = "409", description = "Email is already in use.", content = @Content) })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public StudentResponse create(
      @RequestBody CreateStudentRequest request, HttpServletResponse response) throws EntityExistsException {
    val student = studentService.create(
        request.getName(),
        request.getEmail(),
        request.getPassword());
    response.setHeader("Location", "/students/" + student.getId());
    return new StudentResponse(student);
  }

  @Operation(summary = "Find the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the student."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "student themself")
  @GetMapping("/{studentId}")
  public StudentResponse read(
      @PathVariable Long studentId) throws EntityNotFoundException {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Operation(summary = "Update the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the student.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class))),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "student themself")
  @PutMapping("/{studentId}")
  public StudentResponse update(
      @PathVariable Long studentId) throws EntityNotFoundException {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Operation(summary = "Delete the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deleted the student.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "student themself")
  @DeleteMapping("/{studentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable Long studentId) throws EntityNotFoundException {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Operation(summary = "Find profile of the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the profile."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping("/{studentId}/profile")
  public ProfileResponse readProfile(
      @PathVariable Long studentId) throws EntityNotFoundException {
    return new ProfileResponse(profileService.read(studentId));
  }

  @Operation(summary = "Update profile of the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the profile."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "student themself")
  @PutMapping("/{studentId}/profile")
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
