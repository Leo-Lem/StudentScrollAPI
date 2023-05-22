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
import studentscroll.api.utils.ITestUtils;
import studentscroll.api.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class ChatITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ITestUtils utils;

  @Test
  public void test() throws Exception {
    TestUtils.authenticate(TestUtils.getStudent(1L));

    createChat(Set.of(0L)).andExpect(status().isNotFound());

    val participantIds = utils.createStudents(3);

    val chatId = objectMapper.readTree(
        createChat(participantIds)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString())
        .get("id").asLong();

    getChat(chatId)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(chatId));

    getChat(0L).andExpect(status().isNotFound());

    getChats(1L).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(chatId));

    deleteChat(chatId).andExpect(status().isNoContent());
    deleteChat(chatId).andExpect(status().isNotFound());
  }

  private ResultActions createChat(Set<Long> participantIds) throws Exception {
    return mockMVC.perform(
        post("/chats")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(participantIds)));
  }

  private ResultActions getChat(Long id) throws Exception {
    return mockMVC.perform(get("/chats/" + id));
  }

  private ResultActions deleteChat(Long id) throws Exception {
    return mockMVC.perform(delete("/chats/" + id));
  }

  private ResultActions getChats(Long studentId) throws Exception {
    return mockMVC.perform(get("/chats?participantId=" + studentId));
  }

}
