package studentscroll.api.students.web;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import studentscroll.api.students.services.*;
import studentscroll.api.students.web.dto.*;

@RestController
@RequestMapping("/students")
public class StudentsRestController {

  @Autowired
  private StudentService studentService;

  @Autowired
  private ProfileService profileService;

  // @Autowired
  // private SettingsService settingsService;

  @PostMapping
  public ResponseEntity<?> registerUser(@Validated @RequestBody CreateStudentRequest request) {
    try {
      val student = studentService.create(request.getName(), request.getEmail(), request.getPassword());
      val response = new StudentResponse(student);
      return ResponseEntity.ok().body(response);
    } catch (EntityExistsException e) {
      return ResponseEntity
          .status(409)
          .body("Email is already in use!");
    }
  }

  @GetMapping("/{studentID}/profile")
  public ResponseEntity<?> findProfile(@PathVariable Long studentID) {
    try {
      return ResponseEntity.ok().body(new ProfileResponse(profileService.readProfile(studentID)));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{studentID}/profile")
  public ResponseEntity<?> updateProfile(
      @PathVariable Long studentID, @Validated @RequestBody UpdateProfileRequest request) {
    try {
      val profile = profileService.updateProfile(
          studentID,
          Optional.ofNullable(request.getName()),
          Optional.ofNullable(request.getBio()),
          Optional.ofNullable(request.getIcon()),
          Optional.ofNullable(request.getInterests()).map(Set::of),
          Optional.ofNullable(request.getLocation()));

      return ResponseEntity.ok().body(new ProfileResponse(profile));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

}
