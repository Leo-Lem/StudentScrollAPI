package studentscroll.api.account.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {

  Optional<Student> findByEmail(String email);

  Boolean existsByEmail(String email);
}
