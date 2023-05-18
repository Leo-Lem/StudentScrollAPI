package studentscroll.api.chats.web.dto;

import lombok.*;

@Data
public class CreateMessageRequest {
    @NonNull
    private final String content;

    @NonNull
    private final Long senderId;
}