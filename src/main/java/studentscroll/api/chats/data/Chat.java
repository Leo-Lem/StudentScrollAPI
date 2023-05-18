package studentscroll.api.chats.data;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import studentscroll.api.students.data.Student;

@Entity(name = "chat")
@Data
@NoArgsConstructor
public class Chat {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToMany(mappedBy = "chats")
  private List<Student> participants;

  @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages;
}
