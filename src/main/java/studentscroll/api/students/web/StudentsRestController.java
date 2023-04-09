package studentscroll.api.students.web;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<?> registerStudent(@RequestBody CreateStudentRequest request) {
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

  @GetMapping("/{studentId}/profile")
  public ResponseEntity<?> readProfile(@PathVariable Long studentId) {
    try {
      return ResponseEntity.ok().body(new ProfileResponse(profileService.read(studentId)));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

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
