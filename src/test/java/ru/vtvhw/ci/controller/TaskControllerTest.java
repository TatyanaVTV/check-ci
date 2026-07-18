package ru.vtvhw.ci.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.vtvhw.ci.model.Task;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndGetTask() throws Exception {
        var task = new Task(null, "Test task", false);
        var json = objectMapper.writeValueAsString(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test task"));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test task"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        var task = new Task(null, "To delete", false);
        var json = objectMapper.writeValueAsString(task);
        var response = mockMvc.perform(post("/api/tasks")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getContentAsString();

        var created = objectMapper.readValue(response, Task.class);

        mockMvc.perform(delete("/api/tasks/" + created.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
