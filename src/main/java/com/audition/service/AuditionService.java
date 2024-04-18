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

    public List<AuditionPost> getPosts(AuditionPostFilterDto auditionPostFilterDto) {
        return auditionIntegrationClient.getPosts(auditionPostFilterDto);
    }

    public AuditionPost getPostById(String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    public AuditionPostComments getPostComments(String postId) {
        AuditionPost post = auditionIntegrationClient.getPostById(postId);
        List<AuditionComment> comments = auditionIntegrationClient.getPostComments(postId);
        return AuditionPostComments.builder()
                .postId(post.getId()).title(post.getTitle()).body(post.getBody())
                .comments(comments).build();
    }

    public List<AuditionComment> getComments(AuditionCommentFilterDto auditionCommentFilterDto) {
        return auditionIntegrationClient.getComments(auditionCommentFilterDto);
    }
}
