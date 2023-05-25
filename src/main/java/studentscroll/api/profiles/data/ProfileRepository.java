package studentscroll.api.profiles.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

  List<Profile> findAll();

  List<Profile> findByNameLikeIgnoreCase(String name);

  List<Profile> findByInterestsInIgnoreCase(List<String> interests);

  @Query("SELECT p FROM profile p WHERE p.location.latitude BETWEEN :minLatitude AND :maxLatitude AND p.location.longitude BETWEEN :minLongitude AND :maxLongitude")
  List<Profile> findNearLocation(
      @Param("minLatitude") double minLatitude, @Param("maxLatitude") double maxLatitude,
      @Param("minLongitude") double minLongitude, @Param("maxLongitude") double maxLongitude);

}
