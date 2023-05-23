package studentscroll.api.account.data;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import studentscroll.api.chats.data.Chat;
import studentscroll.api.posts.data.Post;
import studentscroll.api.profiles.data.Profile;

@Entity(name = "account")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class Account implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "email")
  @NonNull
  private String email;

  @Column(name = "password")
  @NonNull
  private String password;

  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @PrimaryKeyJoinColumn
  private Profile profile;

  @Embedded
  private Settings settings = new Settings();

  @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<Post> posts = new ArrayList<>();

  @ManyToMany(mappedBy = "participants", fetch = FetchType.EAGER)
  private List<Chat> chats = new ArrayList<>();

  public Account addPost(Post post) {
    posts.add(post);
    return this;
  }

  public Account removePost(Post post) {
    posts.remove(post);
    return this;
  }

  public Account setProfile(Profile profile) {
    this.profile = profile;
    profile.setAccount(this);
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
