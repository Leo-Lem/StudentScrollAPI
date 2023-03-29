package studentscroll.api.chats.data;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "chatroom")
@Data
public class Chatroom {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

}
