package studentscroll.api.chats.data;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import studentscroll.api.students.data.Student;

@Entity(name = "message")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "content")
  @NonNull
  private String content;

  @Column(name = "timestamp")
  private LocalDateTime timeStamp = LocalDateTime.now();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "sender_id")
  private Student sender;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "chat_id")
  private Chat chat;

}
