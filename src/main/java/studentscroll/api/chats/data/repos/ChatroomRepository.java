package studentscroll.api.chats.data.repos;

import org.springframework.data.repository.CrudRepository;

import studentscroll.api.chats.data.Chatroom;

public interface ChatroomRepository extends CrudRepository<Chatroom, Long> {
}
