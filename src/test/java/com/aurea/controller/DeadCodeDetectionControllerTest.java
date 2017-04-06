package com.aurea.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aurea.model.state.CompletedState;
import com.aurea.model.state.FailedState;
import com.aurea.model.state.ProcessingState;
import com.aurea.service.DeadCodeDetectionService;
import com.aurea.service.ExecutorService;
import com.aurea.util.AbstractDeadCodeDetectionTest;
import java.nio.charset.Charset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@WebMvcTest(DeadCodeDetectionController.class)
public class DeadCodeDetectionControllerTest extends AbstractDeadCodeDetectionTest {

  private static final String LOCATION_HEADER = "Location";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ExecutorService executorService;

  @SpyBean
  private DeadCodeDetectionService deadCodeDetectionService;

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(),
      Charset.forName("utf8"));

  @Before
  public void setup() throws Exception {
  }

  @After
  public void tearDown() {
    deadCodeDetectionService.removeAllRepositories();
  }

  @Test
  public void repositoriesHasNoInitialContent() throws Exception {

    mockMvc.perform(get("/deadcode/repositories")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(content().string("[]"));
  }

  @Test
  public void addRepositoryForInspection() throws Exception {

    mockMvc
        .perform(post("/deadcode/repositories").param("url", repoUrl).contentType(contentType))
        .andExpect(status().isCreated())
        .andExpect(header().string(LOCATION_HEADER, notNullValue()))
        .andExpect(content().string(isEmptyOrNullString()));
  }

  @Test
  public void getRepositoryStatusById() throws Exception {

    String url = postSampleRepository();

    // TODO get id in a dynamic way
    int id = 1;
    mockMvc.perform(get(url + "/status"))
        .andExpect(jsonPath("$.id", is(id)))
        .andExpect(jsonPath("$.gitHubRepoUrl.url", is(repoUrl)))
        .andExpect(jsonPath("$.timeRepoIsAdded").exists())
        .andExpect(jsonPath("$.status").exists())
        .andExpect(jsonPath("$.deadCodeList").doesNotExist());
  }

  @Test
  public void getAllRepositoriesAfterPostNoParameters() throws Exception {

    postSampleRepository();

    String url = "/deadcode/repositories";

    mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.*", hasSize(1)))
        .andExpect(jsonPath("$.[0].id", is(1)))
        .andExpect(jsonPath("$.[0].status").exists())
        .andExpect(jsonPath("$.[0].timeRepoIsAdded").exists())
        .andExpect(jsonPath("$.[0].gitHubRepoUrl.url", is(repoUrl)))
    ;
  }

  @Test
  public void getAllRepositoriesAfterPostWithNegativeLimit() throws Exception {

    postSampleRepository();

    String url = "/deadcode/repositories?limit=-1";

    mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.*", hasSize(1)))
        .andExpect(jsonPath("$.[0].id", is(1)))
        .andExpect(jsonPath("$.[0].status").exists())
        .andExpect(jsonPath("$.[0].timeRepoIsAdded").exists())
        .andExpect(jsonPath("$.[0].gitHubRepoUrl.url", is(repoUrl)))
    ;
  }

  @Test
  public void getAllRepositoriesAfterPostWithNegativePage() throws Exception {

    postSampleRepository();

    String url = "/deadcode/repositories?page=-1";

    mockMvc.perform(get(url))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.*", hasSize(1)))
        .andExpect(jsonPath("$.[0].id", is(1)))
        .andExpect(jsonPath("$.[0].status").exists())
        .andExpect(jsonPath("$.[0].timeRepoIsAdded").exists())
        .andExpect(jsonPath("$.[0].gitHubRepoUrl.url", is(repoUrl)))
    ;
  }

  private String postSampleRepository() throws Exception {

    MvcResult result = mockMvc
        .perform(post("/deadcode/repositories").param("url", repoUrl).contentType(contentType))
        .andReturn();

    return result.getResponse().getHeader(LOCATION_HEADER);
  }

  @Test
  public void postMultipleRepositoriesWithoutConflict() throws Exception {

    mockMvc.perform(post("/deadcode/repositories").param("url", repoUrl).contentType(contentType))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/deadcode/repositories").param("url", repoUrl).contentType(contentType))
        .andExpect(status().isCreated());
  }

  @Test
  public void postRepositoryAndGetWhenItIsCompleted() throws Exception {
    String location = postSampleRepository();

    deadCodeDetection.setState(new CompletedState());
    when(deadCodeDetectionService.get(1L)).thenReturn(deadCodeDetection);

    mockMvc.perform(get(location)).andExpect(status().isOk());
  }

  @Test
  public void postRepositoryAndGetAfterItIsFailed() throws Exception {
    String location = postSampleRepository();

    deadCodeDetection.setState(new FailedState("Error from test!"));
    when(deadCodeDetectionService.get(1L)).thenReturn(deadCodeDetection);

    mockMvc.perform(get(location)).andExpect(status().is5xxServerError());
  }

  @Test
  public void postRepositoryAndGetWhileProcessingNotFinished() throws Exception {
    String location = postSampleRepository();

    deadCodeDetection.setState(new ProcessingState());
    when(deadCodeDetectionService.get(1L)).thenReturn(deadCodeDetection);

    mockMvc.perform(get(location)).andExpect(status().isNotFound());
  }

  @Test
  public void getRepositoryByNonExistingRepoId() throws Exception {

    mockMvc.perform(get("/deadcode/repositories/2344424")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

  }

  @Test
  public void summary() throws Exception {
    postSampleRepository();

    mockMvc.perform(get("/deadcode/repositories/summary")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.*", hasSize(1)));
  }

}