package studentscroll.api.students.data;

import java.time.LocalDate;
import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
  @NonNull
  @Builder.Default
  private LocalDate registeredOn = LocalDate.now();

  @Embedded
  private Profile profile;

  @Embedded
  @Builder.Default
  private Settings settings = new Settings();

  @Override
  public String getPassword() {
    return getPassword();
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
