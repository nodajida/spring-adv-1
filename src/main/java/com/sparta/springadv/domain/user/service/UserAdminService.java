package com.sparta.springadv.domain.user.service;

import com.sparta.springadv.domain.common.exception.InvalidRequestException;
import com.sparta.springadv.domain.user.dto.UserRoleChangeRequest;
import com.sparta.springadv.domain.user.enums.UserRole;
import com.sparta.springadv.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.sparta.springadv.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {
    private final UserRepository userRepository;
    @Transactional
    //public void changeUserRole(long userId, UserRoleChangeRequest userRoleChangeRequest):
    //changeUserRole라는 메서드를 정의
    //역할: 사용자의 역할을 바꾸는 작업
    //입력:
    //long userId: 역할을 바꿀 사용자의 ID
    //UserRoleChangeRequest userRoleChangeRequest: 새로운 역할에 대한 정보를 담고 있는 요청
    public void changeUserRole(Long userId, UserRoleChangeRequest userRoleChangeRequest) {

        //User user = userRepository.findById(userId).orElseThrow(()
        // -> new InvalidRequestException("User not found"));:
        //userRepository.findById(userId): 주어진 userId로 사용자를 찾으려고 합니다.
        //orElseThrow(...): 만약 사용자가 없으면 "User not found"라는 오류를 발생시킵니다.
        //결과: 찾은 사용자를 user 변수에 저장합니다.
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));

        //user.updateRole(UserRole.of(userRoleChangeRequest.getRole()));:
        //user.updateRole(...): 찾은 사용자의 역할을 새로운 역할로 업데이트합니다.
        //UserRole.of(userRoleChangeRequest.getRole()): 요청에서 받은 역할 정보를 UserRole 형식으로 변환합니다.
        user.updateRole(UserRole.of(userRoleChangeRequest.getRole()));
    }
}
