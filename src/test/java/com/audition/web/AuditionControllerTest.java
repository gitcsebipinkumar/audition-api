package com.audition.web;

import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComments;
import com.audition.service.AuditionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.Mockito.when;

@WebMvcTest(AuditionController.class)
class AuditionControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    private transient AuditionService auditionService;

    @Test
    void shouldReturnPosts() throws Exception {
        // Mock the service response
        AuditionPost auditionPost = new AuditionPost();
        auditionPost.setUserId(1);
        auditionPost.setId(1);
        when(auditionService.getPosts(ArgumentMatchers.any())).thenReturn(Collections.singletonList(auditionPost));

        // Perform GET request and verify the response
        String expectedBody = "[{\"userId\":1,\"id\":1}]";
        String responseBody = mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(expectedBody, responseBody);
    }

    @Test
    void shouldReturnPostById() throws Exception {
        // Mock the service response
        when(auditionService.getPostById("1")).thenReturn(new AuditionPost());

        // Perform GET request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturnPostComments() throws Exception {
        // Mock the service response
        when(auditionService.getPostComments("1")).thenReturn(AuditionPostComments.builder().build());

        // Perform GET request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/1/comments")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturnComments() throws Exception {
        // Mock the service response
        when(auditionService.getComments(ArgumentMatchers.any())).thenReturn(Collections.singletonList(new AuditionComment()));

        // Perform GET request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
