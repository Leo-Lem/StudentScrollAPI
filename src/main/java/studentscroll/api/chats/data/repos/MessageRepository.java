package studentscroll.api.chats.data.repos;

import org.springframework.data.repository.CrudRepository;

import studentscroll.api.chats.data.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
