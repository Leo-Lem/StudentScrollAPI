package studentscroll.api.chats.data;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "message")
@Data
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

}
