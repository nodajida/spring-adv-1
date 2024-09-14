package com.sparta.springadv.domain.manager.controller;

import com.sparta.springadv.confing.JwtUtil;
import com.sparta.springadv.domain.common.annotation.Auth;
import com.sparta.springadv.domain.common.dto.AuthUser;
import com.sparta.springadv.domain.manager.dto.ManagerResponse;
import com.sparta.springadv.domain.manager.dto.ManagerSaveRequest;
import com.sparta.springadv.domain.manager.dto.ManagerSaveResponse;
import com.sparta.springadv.domain.manager.service.ManagerService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final JwtUtil jwtUtil;

    //매니저 추가
    @PostMapping("/todos/{todoId}/managers")
    //@Auth AuthUser authUser: 인증된 사용자의 정보를 authUser라는 객체로 받습니다
    //@PathVariable long todoId: 요청 경로의 todoId를 받아서 어떤 할 일에 매니저를 추가할지 식별
    //@Valid @RequestBody ManagerSaveRequest managerSaveRequest: 클라이언트가 보낸 요청 본문을 ManagerSaveRequest 객체로 받고,
    // 유효성 검사를 진행합니다.

    public ResponseEntity<ManagerSaveResponse> saveManager(
            @Auth AuthUser authUser,
            @PathVariable long todoId,
            @Valid @RequestBody ManagerSaveRequest managerSaveRequest
    ) {
        return ResponseEntity.ok(managerService.saveManager(authUser, todoId, managerSaveRequest));
    }
    //매니저 조회
    //@PathVariable long todoId: 요청 경로에서 할 일 ID(todoId)를 받아서 그 할 일의 매니저 목록을 조회합니다.
    //managerService.getManagers(todoId): 서비스 계층에서 매니저 리스트를 가져옵니다.
    //ResponseEntity.ok(): 요청이 성공했을 때 HTTP 200 OK 상태와 함께 매니저 목록을 반환합니다.
    @GetMapping("/todos/{todoId}/managers")
    public ResponseEntity<List<ManagerResponse>> getMembers(@PathVariable long todoId) {
        return ResponseEntity.ok(managerService.getManagers(todoId));
    }
    //매니저 삭제
    //@DeleteMapping("/todos/{todoId}/managers/{managerId}"):
    // "누군가 투두 항목에서 관리자를 삭제하는 요청을 보낼 때, 아래의 작업을 실행해줘"라는 의미
    @DeleteMapping("/todos/{todoId}/managers/{managerId}")
    //public void deleteManager: 이 메서드의 이름이고, 요청을 처리하는 기능
    //void는 반환값이 없다는 뜻
    public void deleteManager(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable long todoId, // URL에서 todo 항목의 고유 ID를 가져오는 방법
            @PathVariable long managerId //URL에서 관리자의 고유 ID를 가져오는 방법
    ) {
        Claims claims = jwtUtil.extractClaims(bearerToken.substring(7));
        long userId = Long.parseLong(claims.getSubject());
        managerService.deleteManager(userId, todoId, managerId);
    }
}
