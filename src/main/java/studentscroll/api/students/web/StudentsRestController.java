package studentscroll.api.students.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityExistsException;
import lombok.val;
import studentscroll.api.students.services.StudentService;
import studentscroll.api.students.web.requestDTO.CreateStudentRequest;
import studentscroll.api.students.web.responseDTO.StudentResponse;

@RestController
@RequestMapping("/students")
public class StudentsRestController {

  @Autowired
  private StudentService service;

  @PostMapping
  public ResponseEntity<?> registerUser(@Validated @RequestBody CreateStudentRequest request) {
    try {
      val student = service.create(request.getName(), request.getEmail(), request.getPassword());
      val response = new StudentResponse(student);
      return ResponseEntity.ok().body(response);
    } catch (EntityExistsException e) {
      return ResponseEntity
          .status(409)
          .body("Email is already in use!");
    }
  }

}
