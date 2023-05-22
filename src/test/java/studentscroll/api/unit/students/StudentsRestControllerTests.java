package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.*;
import lombok.val;
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.students.data.*;
import studentscroll.api.students.services.*;
import studentscroll.api.students.web.StudentsRestController;
import studentscroll.api.students.web.dto.*;

public class StudentsRestControllerTests {

  @Mock
  private ProfileService service;

  @InjectMocks
  private StudentsRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private Profile exampleProfile() {
    return new Profile(
        "James Bond",
        "James Bond is a fictional character created by British novelist Ian Fleming in 1953.",
        "SPORTS_CAR",
        Set.of("SPY", "CARS", "WEAPONS"),
        new ArrayList<>(),
        new ArrayList<>(),
        new StudentLocation("MI6 building", 51.487222, -0.124167));
  }

}
