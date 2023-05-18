package studentscroll.api.chats.web.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import studentscroll.api.chats.data.Message;

@Data
@RequiredArgsConstructor
public class MessageResponse {
    private final Long id;
    private final String content;
    private final Long senderId;
    private final LocalDateTime timestamp;

    public MessageResponse(Message message) {
        this(
                message.getId(),
                message.getContent(),
                message.getSender().getId(),
                message.getTimeStamp());
    }
}
