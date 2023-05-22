package studentscroll.api.students.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import studentscroll.api.account.data.Student;
import studentscroll.api.shared.StudentLocation;

@Embeddable
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Profile {

  @Column(name = "name")
  @NonNull
  private String name;

  @Column(name = "bio")
  private String bio = "";

  @Column(name = "icon")
  private String icon = "default";

  @Column(name = "interests")
  private Set<String> interests = new HashSet<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "student_followers", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "follower_id"))
  private List<Student> followers = new ArrayList<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "student_followers", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
  private List<Student> follows = new ArrayList<>();

  @Embedded
  private StudentLocation location;

  public Optional<StudentLocation> getLocation() {
    return Optional.ofNullable(location);
  }

  public Profile setLocation(Optional<StudentLocation> newLocation) {
    this.location = newLocation.orElse(null);
    return this;
  }

  public Profile addFollower(Student follower) {
    followers.add(follower);
    return this;
  }

}
