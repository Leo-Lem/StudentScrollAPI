package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.students.web.dto.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class RUSettingsITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void puttingAndGettingSettings() throws Exception {
    val id = 1L;

    getSettings(id).andExpect(status().isNotFound());

    updateSettings(id).andExpect(status().isNotFound());

    createStudent().andExpect(jsonPath("$.id").value(id));

    getSettings(id).andExpect(status().isOk());

    updateSettings(id).andExpect(status().isOk());
  }

  private ResultActions getSettings(Long id) throws Exception {
    return mockMVC.perform(get("/students/" + id + "/settings"));
  }

  private ResultActions updateSettings(Long id) throws Exception {
    val request = new UpdateSettingsRequest("DARK", "DE");

    return mockMVC.perform(
        put("/students/" + id + "/settings")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

  private ResultActions createStudent() throws Exception {
    val request = new CreateStudentRequest("John Silver", "abc@xyz.com", "1234");

    return mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

}
