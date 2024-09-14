package com.sparta.springadv.domain.user.controller;

import com.sparta.springadv.domain.user.dto.UserRoleChangeRequest;
import com.sparta.springadv.domain.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    //특정 사용자의 역할을 변경하는 요청
    @PatchMapping("/admin/users/{userId}")
    public void changeUserRole(@PathVariable long userId, @RequestBody UserRoleChangeRequest userRoleChangeRequest) {
        userAdminService.changeUserRole(userId, userRoleChangeRequest);
    }
}
//이 메서드는 관리자가 특정 사용자의 역할을 변경할 수 있도록 해줍니다


