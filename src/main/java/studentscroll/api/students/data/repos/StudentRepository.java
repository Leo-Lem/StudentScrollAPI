package studentscroll.api.students.data.repos;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import studentscroll.api.students.data.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {

  Optional<Student> findByEmail(String email);

}
