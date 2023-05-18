package studentscroll.api.chats.web.dto;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import studentscroll.api.chats.data.Chat;
import studentscroll.api.chats.data.Message;
import studentscroll.api.students.data.Student;

@Data
@RequiredArgsConstructor
public class ChatResponse {
    private final Long id;
    private final List<Long> participantIds;
    private final List<Long> messageIds;

    public ChatResponse(Chat chat) {
        this(
                chat.getId(),
                chat.getParticipants().stream().map(Student::getId).toList(),
                chat.getMessages().stream().map(Message::getId).toList());
    }
}
