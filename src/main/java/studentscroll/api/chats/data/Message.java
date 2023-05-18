package studentscroll.api.chats.data;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import studentscroll.api.students.data.Student;

@Entity(name = "message")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
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

  @JoinColumn(name = "sender_id")
  @ManyToOne(fetch = FetchType.EAGER)
  private Student sender;

  @JoinColumn(name = "chat_id")
  @ManyToOne(fetch = FetchType.EAGER)
  private Chat chat;

}
