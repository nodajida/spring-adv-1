package com.sparta.springadv.domain.user.controller;

import com.sparta.springadv.domain.common.annotation.Auth;
import com.sparta.springadv.domain.common.dto.AuthUser;
import com.sparta.springadv.domain.user.dto.UserChangePasswordRequest;
import com.sparta.springadv.domain.user.dto.UserResponse;
import com.sparta.springadv.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //특정 사용자 ID에 대한 정보를 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }
    //현재 인증된 사용자의 비밀번호를 변경
    @PutMapping("/users")
    public void changePassword(@Auth AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }
}