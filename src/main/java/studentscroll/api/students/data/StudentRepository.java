package studentscroll.api.students.data;

import java.util.*;

import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {

  Optional<Student> findByEmail(String email);

  Boolean existsByEmail(String email);

  // Set<Student> findByProfileName(String name);

}
