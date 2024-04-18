package com.audition.web;

import com.audition.model.AuditionComment;
import com.audition.model.AuditionCommentFilterDto;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComments;
import com.audition.model.AuditionPostFilterDto;
import com.audition.service.AuditionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class AuditionController {

    @Autowired
    private transient AuditionService auditionService;


    /**
     * Retrieves audition posts based on provided filter parameters.
     *
     * @param userId optional user ID filter (must be positive)
     * @param id     optional post ID filter (must be positive)
     * @param title  optional title filter
     * @return list of audition posts matching the filters
     */

    @GetMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get audition posts - Filter data based on provided parameters")
    public List<AuditionPost> getPosts(@RequestParam(required = false) @NumberFormat @Positive String userId,
        @RequestParam(required = false) @NumberFormat @Positive String id,
        @RequestParam(required = false) String title) {

        AuditionPostFilterDto auditionPostFilterDto = new AuditionPostFilterDto(userId, id, title);
        return auditionService.getPosts(auditionPostFilterDto);
    }

    /**
     * Retrieves a specific audition post by its ID.
     *
     * @param id the ID of the audition post to retrieve (must be positive)
     * @return the audition post with the specified ID
     */

    @GetMapping(value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get posts based on Id param")
    public AuditionPost getPostById(@PathVariable @Positive String id) {
        return auditionService.getPostById(id);
    }

    /**
     * Retrieves comments for a specific audition post.
     *
     * @param postId the ID of the audition post for which to retrieve comments (must be positive)
     * @return the post along with its comments
     */

    @GetMapping(value = "posts/{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get comments for a post")
    public AuditionPostComments getPostComments(@PathVariable @Positive String postId) {
        return auditionService.getPostComments(postId);
    }

    /**
     * Retrieves audition comments based on provided filter parameters.
     *
     * @param postId optional post ID filter (must be positive)
     * @param id     optional comment ID filter (must be positive)
     * @param name   optional name filter
     * @param email  optional email filter (must be a valid email address)
     * @return list of audition comments matching the filters
     */

    @GetMapping(value = "comments", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get comments for a post")
    public List<AuditionComment> getComments(@RequestParam(required = false) @NumberFormat @Positive String postId,
        @RequestParam(required = false) @NumberFormat @Positive String id,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) @Email String email) {
        AuditionCommentFilterDto auditionCommentFilterDto = new AuditionCommentFilterDto(postId, id, name, email);
        return auditionService.getComments(auditionCommentFilterDto);
    }

}
