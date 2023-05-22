package studentscroll.api.chats.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.val;
import studentscroll.api.account.data.AccountRepository;
import studentscroll.api.chats.data.Chat;
import studentscroll.api.chats.data.ChatRepository;

@Service
public class ChatService {

  @Autowired
  private ChatRepository repo;

  @Autowired
  private AccountRepository studentRepo;

  public Chat create(
      @NonNull Long studentId,
      @NonNull Set<Long> participants) throws EntityNotFoundException {
    val chat = new Chat();

    participants = new HashSet<>(participants);
    participants.add(studentId);

    participants.forEach(id -> {
      val student = studentRepo.findById(id).orElseThrow(EntityNotFoundException::new);
      chat.addParticipant(student);
    });

    return repo.save(chat);
  }

  public Chat read(@NonNull Long id) throws EntityNotFoundException {
    return repo
        .findById(id)
        .orElseThrow(EntityNotFoundException::new);
  }

  public List<Chat> readByStudentId(
      @NonNull Long studentId) throws EntityNotFoundException {
    return repo.findByParticipantsId(studentId);
  }

  public void delete(@NonNull Long id) throws EntityNotFoundException {
    read(id);

    repo.deleteById(id);
  }

}