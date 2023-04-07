package studentscroll.api.students.data;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "student")
@Data
public class Student implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Embedded
  private Settings settings;

  @Embedded
  private Profile profile;

  @Column(name = "roles")
  private Set<GrantedAuthority> roles;

  // authentication, user details, security, etc.

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
    return this.roles;
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
