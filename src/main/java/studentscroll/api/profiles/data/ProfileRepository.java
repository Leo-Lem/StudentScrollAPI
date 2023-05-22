package studentscroll.api.profiles.data;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
  List<Profile> findAll();

  List<Profile> findByName(String name);

  // TODO: verify this works
  List<Profile> findByInterestsIn(Set<String> interests);

  @Query("SELECT p FROM student_profile p WHERE p.location.latitude BETWEEN :minLatitude AND :maxLatitude AND p.location.longitude BETWEEN :minLongitude AND :maxLongitude")
  List<Profile> findNearLocation(
      @Param("minLatitude") double minLatitude, @Param("maxLatitude") double maxLatitude,
      @Param("minLongitude") double minLongitude, @Param("maxLongitude") double maxLongitude);

}
