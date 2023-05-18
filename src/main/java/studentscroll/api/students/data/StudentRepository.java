package studentscroll.api.students.data;

import java.util.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends CrudRepository<Student, Long> {

  Optional<Student> findByEmail(String email);

  Boolean existsByEmail(String email);

  // Set<Student> findByProfileName(String name);

  @Query("SELECT s FROM student s WHERE s.profile.location.latitude BETWEEN :minLatitude AND :maxLatitude AND s.profile.location.longitude BETWEEN :minLongitude AND :maxLongitude")
  Set<Student> findStudentsNearLocation(
      @Param("minLatitude") double minLatitude, @Param("maxLatitude") double maxLatitude,
      @Param("minLongitude") double minLongitude, @Param("maxLongitude") double maxLongitude);

}
