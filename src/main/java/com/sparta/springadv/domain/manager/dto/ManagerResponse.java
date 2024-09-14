package com.sparta.springadv.domain.manager.dto;

import com.sparta.springadv.domain.user.dto.UserResponse;
import lombok.Getter;

@Getter
public class ManagerResponse {

    private final Long id;
    private final UserResponse user;

    public ManagerResponse(Long id, UserResponse user) {
        this.id = id;
        this.user = user;
    }
}

