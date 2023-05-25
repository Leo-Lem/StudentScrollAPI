package studentscroll.api.profiles.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import studentscroll.api.profiles.data.Profile;
import studentscroll.api.profiles.data.ProfileRepository;
import studentscroll.api.shared.StudentLocation;

@Service
public class ProfileService {

  @Autowired
  private ProfileRepository repo;

  public Profile read(@NonNull Long studentID) throws EntityNotFoundException {
    return repo.findById(studentID).orElseThrow(EntityNotFoundException::new);
  }

  public Profile update(
      @NonNull Profile profile,
      @NonNull Optional<String> newName,
      @NonNull Optional<String> newBio,
      @NonNull Optional<String> newIcon,
      @NonNull Optional<List<String>> newInterests,
      @NonNull Optional<StudentLocation> newLocation) {

    newName.ifPresent(unwrapped -> profile.setName(unwrapped));
    newBio.ifPresent(unwrapped -> profile.setBio(unwrapped));
    newIcon.ifPresent(unwrapped -> profile.setIcon(unwrapped));
    newInterests.ifPresent(unwrapped -> profile.setInterests(unwrapped));
    newLocation.ifPresent(unwrapped -> profile.setLocation(Optional.of(unwrapped)));

    return repo.save(profile);
  }

  public List<Profile> readAllNearLocation(StudentLocation location) {
    double radiusInKm = 10.0; // Fixed radius of 10km

    double latitudeRange = radiusInKm * 0.009; // Roughly 1km in latitude
    double longitudeRange = radiusInKm * 0.014; // Roughly 1km in longitude

    double minLatitude = location.getLatitude() - latitudeRange;
    double maxLatitude = location.getLatitude() + latitudeRange;
    double minLongitude = location.getLongitude() - longitudeRange;
    double maxLongitude = location.getLongitude() + longitudeRange;

    return repo.findNearLocation(minLatitude, maxLatitude, minLongitude, maxLongitude);
  }

  public List<Profile> readByName(@NonNull String name) {
    return repo.findByNameLikeIgnoreCase("%" + name + "%");
  }

  public List<Profile> readByInterests(@NonNull List<String> interests) {
    return repo.findByInterestsInIgnoreCase(interests);
  }

  public List<Profile> readAll() {
    return repo.findAll();
  }

}