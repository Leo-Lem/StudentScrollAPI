package studentscroll.api.chats.web.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import studentscroll.api.chats.data.Chat;
import studentscroll.api.chats.data.Message;

@Data
@RequiredArgsConstructor
public class ChatResponse {
    private final Long id;
    
    public ChatResponse(Message message){
        this(message.getId());
    }
}
