package studentscroll.api.chats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import studentscroll.api.chats.data.Message;
import studentscroll.api.chats.data.MessageRepository;
import studentscroll.api.students.data.StudentRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository repo;

    @Autowired
    private StudentRepository studentRepo;

    public Message create(
            @NonNull String content,
            @NonNull Long senderId,
            @NonNull Long receiverId) throws EntityNotFoundException {

        val message = new Message(content);

        message.setSender(studentRepo
                .findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender does not exist.")));

        return repo.save(message);
    }

    public Message read(
            @NonNull Long id) throws EntityNotFoundException {
        return repo
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    public Message update(
            @NonNull Long id,
            @NonNull String newContent) throws EntityNotFoundException {
        Message message = read(id);
        message.setContent(newContent);
        return repo.save(message);
    }

    public void delete(
            @NonNull Long id) throws EntityNotFoundException {
        if (!repo.existsById(id))
            throw new EntityNotFoundException();

        repo.deleteById(id);
    }
}