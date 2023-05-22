package studentscroll.api.unit.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.students.data.*;
import studentscroll.api.students.services.FollowersService;

public class FollowersServiceTests {

  @Mock
  private StudentRepository repo;

  @InjectMocks
  private FollowersService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private Student exampleStudent() {
    return new Student("raoul@duke.legend", "1234", new Profile("Raoul Duke")).setId(new Random().nextLong());
  }

}
