package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.students.data.Settings;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.web.dto.UpdateSettingsRequest;
import studentscroll.api.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class RUSettingsITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void test() throws Exception {
    val student = new Student().setId(1L).setSettings(new Settings());

    TestUtils.authenticate(student);

    getSettings().andExpect(status().isOk());

    updateSettings().andExpect(status().isOk());
  }

  private ResultActions getSettings() throws Exception {
    return mockMVC.perform(get("/settings"));
  }

  private ResultActions updateSettings() throws Exception {
    val request = new UpdateSettingsRequest("DARK", "DE");

    return mockMVC.perform(
        put("/settings")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

}
