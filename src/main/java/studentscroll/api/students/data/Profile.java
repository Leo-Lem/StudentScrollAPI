package studentscroll.api.students.data;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;
import studentscroll.api.shared.Location;

@Embeddable
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

  @Column(name = "name")
  @NonNull
  private String name;

  @Column(name = "bio")
  private String bio = "";

  @Column(name = "icon")
  private String icon = "PERSON";

  @Column(name = "interests")
  private Set<String> interests = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "student_followers", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "follower_id"))
  private Set<Student> followers = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "student_followers", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
  private Set<Student> follows = new HashSet<>();

  @Embedded
  private Location location;

  public Optional<Location> getLocation() {
    return Optional.ofNullable(location);
  }

  public Profile setLocation(Optional<Location> newLocation) {
    this.location = newLocation.orElse(null);
    return this;
  }

  public Profile addFollower(Student follower) {
    followers.add(follower);
    return this;
  }

}
