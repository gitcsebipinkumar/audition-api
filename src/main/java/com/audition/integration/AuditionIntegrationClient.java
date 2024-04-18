package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionCommentFilterDto;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostFilterDto;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AuditionIntegrationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditionIntegrationClient.class);

    @Value("${postClient.baseUrl}")
    private transient String postUrl;

    @Value("${commentClient.baseUrl}")
    private transient String commentsUrl;

    @Autowired
    private transient RestTemplate restTemplate;

    public List<AuditionPost> getPosts(AuditionPostFilterDto auditionPostFilterDto) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(postUrl)
                .queryParamIfPresent("userId", Optional.ofNullable(auditionPostFilterDto.userId()))
                .queryParamIfPresent("id", Optional.ofNullable(auditionPostFilterDto.id()))
                .queryParamIfPresent("title", Optional.ofNullable(auditionPostFilterDto.title()))
                .build().toUri();

            return restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AuditionPost>>() {
                }).getBody();
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error retrieving posts", e);
            throw new SystemException("Error retrieving posts: " + e.getMessage());
        }
    }

    public AuditionPost getPostById(String id) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(postUrl)
                .pathSegment(id)
                .build().toUri();

            return restTemplate.getForObject(uri, AuditionPost.class);
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found", 404);
            } else {
                throw new SystemException("Error retrieving posts: " + e.getMessage());
            }
        }
    }

    public List<AuditionComment> getPostComments(String postId) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(postUrl)
                .pathSegment(postId, "comments")
                .build().toUri();

            return restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AuditionComment>>() {
                }).getBody();
        } catch (HttpClientErrorException e) {
            throw new SystemException("Error retrieving post comments: " + e.getMessage());
        }
    }

    public List<AuditionComment> getComments(AuditionCommentFilterDto auditionCommentFilterDto) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(commentsUrl)
                .queryParamIfPresent("postId", Optional.ofNullable(auditionCommentFilterDto.postId()))
                .queryParamIfPresent("id", Optional.ofNullable(auditionCommentFilterDto.id()))
                .queryParamIfPresent("name", Optional.ofNullable(auditionCommentFilterDto.name()))
                .queryParamIfPresent("email", Optional.ofNullable(auditionCommentFilterDto.email()))
                .build().toUri();
            return restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AuditionComment>>() {
                }).getBody();
        } catch (HttpClientErrorException e) {
            throw new SystemException("Error retrieving comments: " + e.getMessage());
        }
    }
}
