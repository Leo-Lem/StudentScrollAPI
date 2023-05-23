package studentscroll.api.chats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.val;
import studentscroll.api.account.data.Account;
import studentscroll.api.chats.data.ChatRepository;
import studentscroll.api.chats.data.Message;
import studentscroll.api.chats.data.MessageRepository;

@Service
public class MessageService {
  @Autowired
  private MessageRepository repo;

  @Autowired
  private ChatRepository chatRepo;

  public Message create(
      @NonNull Account sender,
      @NonNull String content,
      @NonNull Long chatId) throws EntityNotFoundException {
    val chat = chatRepo
        .findById(chatId)
        .orElseThrow(() -> new EntityNotFoundException("Chat does not exist."));

    val message = new Message(content);
    message.setSender(sender);
    message.setChat(chat);

    return repo.save(message);
  }

  public Message read(@NonNull Long id) throws EntityNotFoundException {
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

  public void delete(@NonNull Long id) throws EntityNotFoundException {
    Message message = read(id);
    chatRepo.save(message.getChat().removeMessage(message));
  }

}