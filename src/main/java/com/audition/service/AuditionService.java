package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionCommentFilterDto;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComments;
import com.audition.model.AuditionPostFilterDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditionService {

    @Autowired
    private transient AuditionIntegrationClient auditionIntegrationClient;

    /**
     * Retrieves audition posts based on the provided filter criteria.
     *
     * @param auditionPostFilterDto the filter criteria for retrieving audition posts
     * @return a list of audition posts matching the filter criteria
     */
    public List<AuditionPost> getPosts(AuditionPostFilterDto auditionPostFilterDto) {
        return auditionIntegrationClient.getPosts(auditionPostFilterDto);
    }

    /**
     * Retrieves a specific audition post by its ID.
     *
     * @param postId the ID of the audition post to retrieve
     * @return the audition post with the specified ID, or {@code null} if not found
     */
    public AuditionPost getPostById(String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    /**
     * Retrieves audition post comments for a specific post ID.
     *
     * @param postId the ID of the audition post for which to retrieve comments
     * @return the audition post along with its comments
     */

    public AuditionPostComments getPostComments(String postId) {
        AuditionPost post = auditionIntegrationClient.getPostById(postId);
        List<AuditionComment> comments = auditionIntegrationClient.getPostComments(postId);
        return AuditionPostComments.builder()
                .postId(post.getId()).title(post.getTitle()).body(post.getBody())
                .comments(comments).build();
    }

    /**
     * Retrieves audition comments based on the provided filter criteria.
     *
     * @param auditionCommentFilterDto the filter criteria for retrieving audition comments
     * @return a list of audition comments matching the filter criteria
     */

    public List<AuditionComment> getComments(AuditionCommentFilterDto auditionCommentFilterDto) {
        return auditionIntegrationClient.getComments(auditionCommentFilterDto);
    }
}
