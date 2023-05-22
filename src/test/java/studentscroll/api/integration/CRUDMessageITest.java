package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.chats.web.dto.CreateMessageRequest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class CRUDMessageITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  // @Test
  // public void test() throws Exception {
  // val participantIds = createStudents();
  // val chatId = createChat(participantIds);

  // sendMessage(chatId, 0L).andExpect(status().isNotFound());
  // sendMessage(0L,
  // participantIds.iterator().next()).andExpect(status().isNotFound());

  // val senderId = participantIds.iterator().next();
  // val messageId = objectMapper.readTree(
  // sendMessage(chatId, senderId)
  // .andExpect(status().isCreated())
  // .andReturn().getResponse().getContentAsString())
  // .get("id").asLong();

  // getMessage(chatId,
  // messageId).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(messageId));

  // updateMessage(chatId, messageId).andExpect(status().isOk());

  // deleteMessage(chatId, messageId).andExpect(status().isNoContent());
  // deleteMessage(chatId, messageId).andExpect(status().isNotFound());
  // }

  private ResultActions sendMessage(Long chatId, Long senderId) throws Exception {
    return mockMVC.perform(
        post("/chats/" + chatId + "/messages")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(new CreateMessageRequest("some message", senderId))));
  }

  private ResultActions getMessage(Long chatId, Long messageId) throws Exception {
    return mockMVC.perform(get("/chats/" + chatId + "/messages/" + messageId));
  }

  private ResultActions updateMessage(Long chatId, Long messageId) throws Exception {
    return mockMVC.perform(
        put("/chats/" + chatId + "/messages/" + messageId)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString("some new message")));
  }

  private ResultActions deleteMessage(Long chatId, Long messageId) throws Exception {
    return mockMVC.perform(delete("/chats/" + chatId + "/messages/" + messageId));
  }

  private Long createChat(Set<Long> participantIds) throws Exception {
    return objectMapper.readTree(
        mockMVC.perform(
            post("/chats")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(participantIds)))
            .andReturn().getResponse().getContentAsString())
        .get("id").asLong();
  }

}
