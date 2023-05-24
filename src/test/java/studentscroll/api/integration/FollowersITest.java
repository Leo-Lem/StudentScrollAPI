package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import studentscroll.api.utils.ITestUtils;
import studentscroll.api.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class FollowersITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ITestUtils utils;

  @Test
  public void test() throws Exception {
    Long firstStudentId = utils.createStudent();
    Long secondStudentId = utils.createStudent();

    TestUtils.authenticate(TestUtils.getStudent(firstStudentId));

    follow(secondStudentId).andExpect(status().isCreated());

    unfollow(secondStudentId).andExpect(status().isNoContent());
  }

  private ResultActions follow(Long studentId) throws Exception {
    return mockMVC.perform(post("/students/" + studentId + "/followers"));
  }

  private ResultActions unfollow(Long studentId) throws Exception {
    return mockMVC.perform(delete("/students/" + studentId + "/followers"));
  }

}
