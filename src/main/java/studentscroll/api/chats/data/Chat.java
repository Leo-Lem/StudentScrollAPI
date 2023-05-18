package studentscroll.api.chats.data;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import studentscroll.api.students.data.Student;

@Entity(name = "chat")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Chat {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "student_chat", joinColumns = @JoinColumn(name = "chat_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
  private List<Student> participants = new ArrayList<>();

  @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @NonNull
  private List<Message> messages = new ArrayList<>();

  public Chat addParticipant(Student student) {
    participants.add(student);
    student.getChats().add(this);
    return this;
  }

  public Chat removeParticipant(Student student) {
    participants.remove(student);
    student.getChats().remove(this);
    return this;
  }

  public Chat addMessage(Message message) {
    messages.add(message);
    message.setChat(this);
    return this;
  }

  public Chat removeMessage(Message message) {
    messages.remove(message);
    message.setChat(null);
    return this;
  }
}
