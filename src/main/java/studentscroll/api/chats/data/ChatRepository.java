package studentscroll.api.chats.data;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends CrudRepository<Chat, Long> {
    List<Chat> findByParticipantsId(Long id);
}
