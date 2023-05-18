package studentscroll.api.students.data;

import java.time.LocalDate;
import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import studentscroll.api.chats.data.Chat;
import studentscroll.api.posts.data.Post;

@Entity(name = "student")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class Student implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "email")
  @NonNull
  private String email;

  @Column(name = "password")
  @NonNull
  private String password;

  @Column(name = "registeredOn")
  private LocalDate registeredOn = LocalDate.now();

  @Embedded
  @NonNull
  private Profile profile;

  @Embedded
  private Settings settings = new Settings();

  @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Post> posts;

  @ManyToMany(mappedBy = "participants")
  private List<Chat> chats = new ArrayList<>();

  public Student addPost(Post post) {
    posts.add(post);
    return this;
  }

  // security

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return getEmail();
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return Set.of(new SimpleGrantedAuthority("USER"));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
