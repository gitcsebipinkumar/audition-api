package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;

import com.audition.model.AuditionCommentFilterDto;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComments;
import com.audition.model.AuditionPostFilterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuditionServiceTest {

    @Mock
    private transient AuditionIntegrationClient auditionIntegrationClient;

    @InjectMocks
    private transient AuditionService auditionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPosts() {
        // Mock data
        AuditionPost post = new AuditionPost();
        post.setId(1);
        post.setTitle("Test Post1");
        post.setBody("Test Body2");
        AuditionPostFilterDto filterDto = new AuditionPostFilterDto("1", "1", "test");
        when(auditionIntegrationClient.getPosts(any())).thenReturn(Arrays.asList(post));

        // Call service method
        List<AuditionPost> posts = auditionService.getPosts(filterDto);

        // Verify
        assertEquals(1, posts.size());
        AuditionPost resultPost = posts.get(0);
        assertEquals(1, resultPost.getId());
        assertEquals("Test Post1", resultPost.getTitle());
        assertEquals("Test Body2", resultPost.getBody());
    }

    @Test
    public void testGetPostById() {
        // Mock data
        int postId = 1;
        AuditionPost post = new AuditionPost();
        post.setId(postId);
        post.setTitle("Test Post4");
        post.setBody("Test Body4");

        when(auditionIntegrationClient.getPostById(String.valueOf(postId))).thenReturn(post);

        // Call service method
        AuditionPost resultPost = auditionService.getPostById(String.valueOf(postId));

        // Verify
        assertEquals(postId, resultPost.getId());
        assertEquals("Test Post4", resultPost.getTitle());
        assertEquals("Test Body4", resultPost.getBody());
    }

    @Test
    public void testGetPostComments() {
        // Mock data
        int postId = 1;
        AuditionPost post = new AuditionPost();
        post.setId(postId);
        post.setTitle("Test Post5");
        post.setBody("Test Body5");

        AuditionComment comment1 = new AuditionComment();
        comment1.setId(1);
        comment1.setName("Bipin kumar1");
        comment1.setEmail("bk1@example.com");
        comment1.setBody("Comment 1");

        AuditionComment comment2 = new AuditionComment();
        comment2.setId(2);
        comment2.setName("Bipin kumar2");
        comment2.setEmail("bk2@example.com");
        comment2.setBody("Comment 2");

        List<AuditionComment> comments = Arrays.asList(comment1, comment2);

        when(auditionIntegrationClient.getPostById(String.valueOf(postId))).thenReturn(post);
        when(auditionIntegrationClient.getPostComments(String.valueOf(postId))).thenReturn(comments);

        // Call service method
        AuditionPostComments postComments = auditionService.getPostComments(String.valueOf(postId));

        // Verify
        assertEquals(postId, postComments.getPostId());
        assertEquals("Test Post5", postComments.getTitle());
        assertEquals("Test Body5", postComments.getBody());
        assertEquals(2, postComments.getComments().size());
        assertEquals("Bipin kumar1", postComments.getComments().get(0).getName());
        assertEquals("Comment 1", postComments.getComments().get(0).getBody());
        assertEquals("Bipin kumar2", postComments.getComments().get(1).getName());
        assertEquals("Comment 2", postComments.getComments().get(1).getBody());
    }

    @Test
    public void testGetComments() {
        // Mock data
        AuditionComment comment = new AuditionComment();
        comment.setId(1);
        comment.setName("Bipin kumar");
        comment.setEmail("bk@example.com");
        comment.setBody("Test Comment");

        when(auditionIntegrationClient.getComments(any())).thenReturn(Arrays.asList(comment));

        AuditionCommentFilterDto filterDto = new AuditionCommentFilterDto("1", "1", "name", "email@abc.com");
        // Call service method
        List<AuditionComment> comments = auditionService.getComments(filterDto);

        // Verify
        assertEquals(1, comments.size());
        AuditionComment resultComment = comments.get(0);
        assertEquals(1, resultComment.getId());
        assertEquals("Bipin kumar", resultComment.getName());
        assertEquals("bk@example.com", resultComment.getEmail());
        assertEquals("Test Comment", resultComment.getBody());
    }
}
