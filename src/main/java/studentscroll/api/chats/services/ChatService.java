package studentscroll.api.chats.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import studentscroll.api.chats.data.*;
import studentscroll.api.students.data.StudentRepository;

@Service
public class ChatService {

  @Autowired
  private ChatRepository repo;

  @Autowired
  private StudentRepository studentRepo;

  public Chat create(
      @NonNull Set<Long> participants) throws EntityNotFoundException {
    val chat = new Chat();

    participants.forEach(id -> {
      val student = studentRepo.findById(id).orElseThrow(EntityNotFoundException::new);
      chat.addParticipant(student);
    });

    return repo.save(chat);
  }

}