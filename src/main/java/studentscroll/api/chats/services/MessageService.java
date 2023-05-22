package studentscroll.api.chats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.chats.data.*;

@Service
public class MessageService {
  @Autowired
  private MessageRepository repo;

  @Autowired
  private StudentRepository studentRepo;

  @Autowired
  private ChatRepository chatRepo;

  public Message create(
      @NonNull String content,
      @NonNull Long senderId,
      @NonNull Long chatId) throws EntityNotFoundException {

    val message = new Message(content);

    message.setSender(studentRepo
        .findById(senderId)
        .orElseThrow(() -> new EntityNotFoundException("Sender does not exist.")));

    message.setChat(chatRepo
        .findById(chatId)
        .orElseThrow(() -> new EntityNotFoundException("Chat does not exist.")));

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

  @Transactional
  public void delete(
      @NonNull Long id) throws EntityNotFoundException {
    val message = read(id);
    chatRepo.save(message.getChat().removeMessage(message));
  }
}