package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import studentscroll.api.utils.ITestUtils;
import studentscroll.api.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class MessageITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ITestUtils utils;

  @Test
  public void test() throws Exception {
    TestUtils.authenticate(TestUtils.getStudent(1L));

    val participantIds = utils.createStudents(3);
    val chatId = createChat(participantIds);

    sendMessage(0L).andExpect(status().isNotFound());

    val messageId = objectMapper.readTree(
        sendMessage(chatId)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString())
        .get("id").asLong();

    getMessage(chatId,
        messageId).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(messageId));

    updateMessage(chatId, messageId).andExpect(status().isOk());

    deleteMessage(chatId, messageId).andExpect(status().isNoContent());
    deleteMessage(chatId, messageId).andExpect(status().isNotFound());
  }

  private ResultActions sendMessage(Long chatId) throws Exception {
    return mockMVC.perform(
        post("/chats/" + chatId + "/messages")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString("some message")));
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
