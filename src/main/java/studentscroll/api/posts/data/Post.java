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
@RequiredArgsConstructor
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "poster_id")
  @NonNull
  private Long posterId;

  @Column(name = "timestamp")
  private final LocalDate timestamp = LocalDate.now();

  @Column(name = "title")
  @NonNull
  private String title;

  @Column(name = "tags")
  @NonNull
  private Set<String> tags;

  @JoinColumn(name = "poster_id", nullable = false, insertable = false, updatable = false)
  @ManyToOne
  private Student poster;

}
