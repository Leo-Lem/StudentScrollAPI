package studentscroll.api.posts.data;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import studentscroll.api.students.data.Student;

@Entity(name = "post")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "timestamp")
  private final LocalDateTime timestamp = LocalDateTime.now();

  @Column(name = "title")
  @NonNull
  private String title;

  @Column(name = "tags")
  @NonNull
  private Set<String> tags;

  @JoinColumn(name = "student_id")
  @ManyToOne(fetch = FetchType.EAGER)
  private Student poster;

}
