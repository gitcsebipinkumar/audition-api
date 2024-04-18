package com.audition.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class AuditionPostComments {

    private int postId;
    private int id;
    private String title;
    private String body;
    @Singular
    private List<AuditionComment> comments;
}