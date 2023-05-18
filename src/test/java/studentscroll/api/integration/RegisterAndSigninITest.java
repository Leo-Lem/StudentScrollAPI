package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.security.auth.SigninRequest;
import studentscroll.api.students.web.dto.CreateStudentRequest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class RegisterAndSigninITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  private String email = "jwayne@xyz.com", password = "1234";

  @Test
  public void test() throws Exception {
    createStudent().andExpect(status().isCreated());

    createStudent().andExpect(status().isConflict());

    signIn().andExpect(status().isOk());
  }

  private ResultActions createStudent() throws Exception {
    val request = new CreateStudentRequest("John Wayne", email, password);
    return mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

  private ResultActions signIn() throws Exception {
    val request = new SigninRequest(email, password);

    return mockMVC.perform(
        post("/signin")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

}
