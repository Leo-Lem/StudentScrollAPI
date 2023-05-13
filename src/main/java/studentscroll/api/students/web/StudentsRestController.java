package studentscroll.api.students.web;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import studentscroll.api.security.JSONWebToken;
import studentscroll.api.students.services.*;
import studentscroll.api.students.web.dto.*;

@Tag(name = "Students", description = "Everything related to students.")
@RestController
@RequestMapping("/students")
public class StudentsRestController {

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private StudentService studentService;

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
    return new StudentResponse(student, JSONWebToken.generateFrom(student));
  }

  @Operation(summary = "Find the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the student."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping("/{studentId}")
  public StudentResponse read(
      @PathVariable Long studentId) throws EntityNotFoundException {
    return new StudentResponse(studentService.read(studentId));
  }

  @Operation(summary = "Update the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the student."),
      @ApiResponse(responseCode = "401", description = "Current password is wrong.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PutMapping("/{studentId}")
  public StudentResponse update(
      @PathVariable Long studentId, @RequestBody UpdateStudentRequest request) throws EntityNotFoundException {
    var student = studentService.read(studentId);

    if (!authManager
        .authenticate(new UsernamePasswordAuthenticationToken(student.getEmail(), request.getCurrentPassword()))
        .isAuthenticated())
      throw new BadCredentialsException("Invalid password.");

    student = studentService.update(
        studentId, Optional.ofNullable(request.getNewEmail()), Optional.ofNullable(request.getNewPassword()));

    return new StudentResponse(student, JSONWebToken.generateFrom(student));
  }

  @Operation(summary = "Delete the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deleted the student.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @DeleteMapping("/{studentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable Long studentId) throws EntityNotFoundException {
    studentService.delete(studentId);
  }

}
