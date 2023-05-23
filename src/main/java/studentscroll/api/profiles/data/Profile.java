package studentscroll.api.profiles.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import studentscroll.api.account.data.Account;
import studentscroll.api.shared.StudentLocation;

@Entity(name = "profile")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Profile {

  @Id
  private Long id;

  @Column(name = "name")
  @NonNull
  private String name;

  @Column(name = "bio")
  private String bio = "";

  @Column(name = "icon")
  private String icon = "default";

  @Column(name = "interests")
  private List<String> interests = new ArrayList<>();

  @Column(name = "registeredOn")
  private final LocalDateTime registeredOn = LocalDateTime.now();

  @MapsId
  @OneToOne(fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Account account;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "profile_follows", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "follow_id"))
  @ToString.Exclude
  private List<Profile> followers = new ArrayList<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "profile_follows", joinColumns = @JoinColumn(name = "follow_id"), inverseJoinColumns = @JoinColumn(name = "follower_id"))
  @ToString.Exclude
  private List<Profile> follows = new ArrayList<>();

  @Embedded
  private StudentLocation location;

  public Optional<StudentLocation> getLocation() {
    return Optional.ofNullable(location);
  }

  public Profile setLocation(Optional<StudentLocation> newLocation) {
    this.location = newLocation.orElse(null);
    return this;
  }

  public Profile addFollow(Profile follow) {
    follows.add(follow);
    follow.followers.add(follow);
    return this;
  }

  public Profile removeFollow(Profile follow) {
    follows.remove(follow);
    follow.followers.remove(this);
    return this;
  }

}
