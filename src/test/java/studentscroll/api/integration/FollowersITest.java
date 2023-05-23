package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    getFollowers(1L).andExpect(status().isNotFound());
    getFollows(1L).andExpect(status().isNotFound());

    Long firstStudentId = utils.createStudent();
    Long secondStudentId = utils.createStudent();

    TestUtils.authenticate(TestUtils.getStudent(firstStudentId));

    getFollowers(firstStudentId).andExpect(content().json("[]"));
    getFollows(firstStudentId).andExpect(content().json("[]"));

    follow(secondStudentId).andExpect(status().isCreated());

    unfollow(secondStudentId).andExpect(status().isNoContent());
  }

  private ResultActions getFollowers(Long id) throws Exception {
    return mockMVC.perform(get("/students/" + id + "/followers"));
  }

  private ResultActions getFollows(Long id) throws Exception {
    return mockMVC.perform(get("/students/" + id + "/follows"));
  }

  private ResultActions follow(Long studentId) throws Exception {
    return mockMVC.perform(post("/students/" + studentId + "/followers"));
  }

  private ResultActions unfollow(Long studentId) throws Exception {
    return mockMVC.perform(delete("/students/" + studentId + "/followers"));
  }

}
