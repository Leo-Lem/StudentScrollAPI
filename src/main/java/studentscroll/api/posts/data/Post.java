package studentscroll.api.posts.data;

import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;
import studentscroll.api.students.data.Student;

@Entity(name = "post")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "title")
  @NonNull
  private String name;

  @Column(name = "timestamp")
  private LocalDate timestamp = LocalDate.now();

  @Column(name = "tags")
  private Set<String> interests = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id", nullable = false)
  private Student poster;

}
