package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CRDChatITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void crdChatITest() throws Exception {

    val participantIds = Set.of(1L, 2L, 3L);

    mockMVC.perform(
        post("/chats")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(participantIds)))
        .andExpect(status().isNotFound());

  }

}
