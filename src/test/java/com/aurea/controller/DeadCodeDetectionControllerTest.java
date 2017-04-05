package com.aurea.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.state.InitialState;
import com.aurea.service.DeadCodeDetectionService;
import com.aurea.service.ExecutorService;
import java.nio.charset.Charset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(DeadCodeDetectionController.class)
public class DeadCodeDetectionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ExecutorService executorService;

  @MockBean
  private DeadCodeDetectionService deadCodeDetectionService;

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(),
      Charset.forName("utf8"));

  private JacksonTester<DeadCodeDetection> json;


  @Before
  public void setup() throws Exception {
//    String jsonValue = this.json.write(getDeadCodeDetection()).getJson();
    this.mockMvc
        .perform(post("/deadcode/add")
            .content(repoUrl).contentType(contentType));
  }

  private final String repoUrl = "https://github.com/sediyev/dead-code-detector.git";

  private DeadCodeDetection getDeadCodeDetection() {
    DeadCodeDetection deadCodeDetection = new DeadCodeDetection(repoUrl);
    deadCodeDetection.setId(10L);
    deadCodeDetection.setState(new InitialState());

    return deadCodeDetection;
  }


  @Test
  public void addRepositoryForInspection() throws Exception {

    when(deadCodeDetectionService.create(repoUrl)).thenReturn(getDeadCodeDetection());

    mockMvc.perform(get("/deadcode/repositories")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
    .andExpect(content().string("[]"));
  }

  @Test
  public void getRepositories() throws Exception {
  }

  @Test
  public void getRepositoryById() throws Exception {
  }

  @Test
  public void summary() throws Exception {
  }

}