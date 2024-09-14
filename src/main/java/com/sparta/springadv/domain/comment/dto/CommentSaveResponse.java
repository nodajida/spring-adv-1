package com.sparta.springadv.domain.comment.dto;

import com.sparta.springadv.domain.user.dto.UserResponse;
import lombok.Getter;

@Getter
public class CommentSaveResponse {

    private final Long id;
    private final String contents;
    private final UserResponse user;

    public CommentSaveResponse(Long id, String contents, UserResponse user) {
        this.id = id;
        this.contents = contents;
        this.user = user;
    }
}