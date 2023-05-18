package studentscroll.api.chats.web.dto;

import java.util.Set;

import lombok.*;

@Data
public class CreateChatRequest {

    @NonNull
    private final Set<Long> participantIds;

}