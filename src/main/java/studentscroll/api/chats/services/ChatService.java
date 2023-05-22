package studentscroll.api.chats.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.val;
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.chats.data.Chat;
import studentscroll.api.chats.data.ChatRepository;

@Service
public class ChatService {

  @Autowired
  private ChatRepository repo;

  @Autowired
  private StudentRepository studentRepo;

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

  public Chat read(
      @NonNull Long studentId,
      @NonNull Long id) throws EntityNotFoundException {
    val chat = repo
        .findById(id)
        .orElseThrow(EntityNotFoundException::new);

    return chat;
  }

  public List<Chat> readByStudentId(
      @NonNull Long studentId) throws EntityNotFoundException {
    return repo.findByParticipantsId(studentId);
  }

  public void delete(
      @NonNull Long studentId,
      @NonNull Long id) throws EntityNotFoundException {
    read(studentId, id);

    repo.deleteById(id);
  }

}