package com.sparta.springadv.domain.todo.controller;

import com.sparta.springadv.domain.common.annotation.Auth;
import com.sparta.springadv.domain.common.dto.AuthUser;
import com.sparta.springadv.domain.todo.dto.TodoResponse;
import com.sparta.springadv.domain.todo.dto.TodoSaveRequest;
import com.sparta.springadv.domain.todo.dto.TodoSaveResponse;
import com.sparta.springadv.domain.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // 게시물 생성
    @PostMapping("/todos")
    //  public ResponseEntity<TodoSaveResponse>saveTodo(...)
    // 이 부분은 우리가 어떤 일을 하기 위한 준비를 하는 거라고 생각하자
    // 여기서 saveTodo라는 행동을 하는데 '할 일'을 저장하는 걸 의미
    // 여기서 주인공은 saveTodo라는 함수야
    //함수는 우리가 시키는 일을 대신해주는 역할이야
    // ResponseEntity<TodoSaveResponse>: 이건 함수가 일을 끝낸 후 우리에게 돌려줄 값
    // odoSaveResponse라는 결과를 돌려주는데 이건 우리가 저장한 '할 일'에 대한 정보
    // @Auth AuthUser authUser 이 부분은 누구의 '할 일'인지 확인하는 단계
    // 'authUser'는 할 일을 저장하는 사용자가 누구인지 알려주는 정보
    // 에시를 들자면 내가 학교에 들어갈 때 이름표를 주는 것처럼 여기에서는 이 '할 일'이 누구 것인지 알려줘야 해
    // @Auth: 이건 사용자가 진짜로 맞는지 확인해주는 표시
    //@Valid @RequestBody TodoSaveRequest todoSaveRequest는 여기서 '할 일'을 구체적으로 적어야 해. 우리가 무엇을 할지 종이에 적어 제출하는 것처럼,
    // 'todoSaveRequest'는 네가 저장할 '할 일'의 제목이나 내용을 적은 종이야.
    //@Valid: 이건 '할 일'이 제대로 적혔는지 확인해주는 선생님 같은 역할이야. 틀린 게 없는지 검사해줘.
    //@RequestBody: 이건 '할 일'을 적은 내용을 받아온다는 뜻이야. 즉, 우리가 직접 적은 '할 일'의 내용이 여기에 담기는 거야.
    // return ResponseEntity.ok(todoService.saveTodo(authUser, todoSaveRequest));이제 이 함수가 하는 마지막 일은 저장한
    // '할 일'을 돌려주는 거야. 우리가 제출한 '할 일'이 제대로 저장됐다는 걸 알려주는 거지.
    // ResponseEntity.ok: 이건 "모두 잘 됐다!"고 알려주는 신호야. '할 일'이 잘 저장되었다고 말해주는 거지.
    //todoService.saveTodo(authUser, todoSaveRequest): 이건 진짜로 '할 일'을 저장하는 부분이야. 'authUser'는 누구의 '할 일'인지,
    // 'todoSaveRequest'는 저장할 내용을 담고 있어.
    // 결론: 쉽게 말하면, 이 함수는 누가 어떤 '할 일'을 만들었는지 받아와서, 그걸 저장하고 잘 됐다고 알려주는 역할을 해!

    public ResponseEntity<TodoSaveResponse>saveTodo(@Auth AuthUser authUser, @Valid @RequestBody TodoSaveRequest todoSaveRequest){
        return ResponseEntity.ok(todoService.saveTodo(authUser, todoSaveRequest));
    }

    //모든 할 일의 목록을 가져옵니다.
    @GetMapping("/todos")
    public ResponseEntity<Page<TodoResponse>> getTodos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(todoService.getTodos(page, size));
    }
    //특정 할 일을 가져온다
    @GetMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable long todoId) {
        return ResponseEntity.ok(todoService.getTodo(todoId));
    }
}

