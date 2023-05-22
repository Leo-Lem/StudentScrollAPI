package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import studentscroll.api.account.data.Account;
import studentscroll.api.account.web.dto.AuthenticationRequest;
import studentscroll.api.account.web.dto.UpdateCredentialsRequest;
import studentscroll.api.profiles.data.Profile;
import studentscroll.api.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class AccountITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  private String email = "jwayne@xyz.com", password = "1234";

  @Test
  public void test() throws Exception {
    signUp().andExpect(status().isCreated());

    signUp().andExpect(status().isConflict());

    signIn().andExpect(status().isOk());

    TestUtils.authenticate(new Account().setProfile(new Profile("")));

    updateStudent().andExpect(status().isOk());

    deleteStudent().andExpect(status().isNoContent());
  }

  private ResultActions signUp() throws Exception {
    val request = new AuthenticationRequest("John Wayne", email, password);
    return mockMVC.perform(
        post("/account")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

  private ResultActions signIn() throws Exception {
    val request = new AuthenticationRequest(null, email, password);

    return mockMVC.perform(
        post("/account")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

  private ResultActions updateStudent() throws Exception {
    val request = new UpdateCredentialsRequest(email, password, "xyz@abc.com", null);
    return mockMVC.perform(
        put("/account")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

  private ResultActions deleteStudent() throws Exception {
    return mockMVC.perform(
        delete("/account"));
  }

}
