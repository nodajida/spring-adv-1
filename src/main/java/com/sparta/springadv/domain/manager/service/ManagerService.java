package com.sparta.springadv.domain.manager.service;

import com.sparta.springadv.domain.common.dto.AuthUser;
import com.sparta.springadv.domain.common.exception.InvalidRequestException;
import com.sparta.springadv.domain.manager.dto.ManagerResponse;
import com.sparta.springadv.domain.manager.dto.ManagerSaveRequest;
import com.sparta.springadv.domain.manager.dto.ManagerSaveResponse;
import com.sparta.springadv.domain.manager.entity.Manager;
import com.sparta.springadv.domain.manager.repository.ManagerRepository;
import com.sparta.springadv.domain.todo.entity.Todo;
import com.sparta.springadv.domain.todo.repository.TodoRepository;
import com.sparta.springadv.domain.user.dto.UserResponse;
import com.sparta.springadv.domain.user.entity.User;
import com.sparta.springadv.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
//이 서비스는 어떤 사람이 일정을 관리할 수 있도록 도와주고, 담당자를 추가하거나 삭제하는 기능을 제공
public class ManagerService {
    //데이터베이스에서 정보를 가져오고, 저장하는 역할을 하는 도구
    //managerRepository: 일정 담당자 정보를 저장하거나 찾는 도구
    //userRepository: 사용자(사람) 정보를 저장하거나 찾는 도구
    //todoRepository: 일정 정보를 저장하거나 찾는 도구
    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
//    담당자 추가하는 함수: saveManager
//    이 함수는 일정에 새로운 담당자를 추가하는 역할을 해요.
//    먼저, 일정 작성자(일정을 만든 사람)의 정보를 찾아요.
//    그리고 그 일정에 새로운 담당자를 등록해요.
    public ManagerSaveResponse saveManager(AuthUser authUser, long todoId, ManagerSaveRequest managerSaveRequest) {
        // 일정을 만든 유저
        //여기서 "일정을 만든 사람"의 정보를 가져와요. 이 사람을 user라고 부를게요.
        User user = User.fromAuthUser(authUser);

       //일정 정보 찾기
        //
        //todoRepository.findById(todoId): 이 부분에서는 일정 번호(todoId)를 가지고 그 일정이 존재하는지 확인해요.
        //만약 일정이 없다면 오류를 내요. (오류는 프로그램이 잘못된 요청을 받았다고 알려주는 것)
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        //작성자가 맞는지 확인
        //if (!ObjectUtils.nullSafeEquals(...)): 이 부분은 "요청한 사람이 이 일정을 만든 사람인지 확인"하는 부분이에요.
        // 작성자가 아니면 오류를 던져요.

        // if ... throw:요청한 사람이 일정의 작성자인지 확인하고, 아니라면 오류를 발생시킨다.
        //!ObjectUtils.nullSafeEquals(a, b)는 a와 b가 같지 않다는 것을 의미해요.
        //두 값이 null일 때도 오류 없이 안전하게 처리하면서 비교하고, 같지 않으면 true를 반환하는 거예요.
        if (!ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new InvalidRequestException("담당자를 등록하려고 하는 유저가 일정을 만든 유저가 유효하지 않습니다!");
        }
        // 새로운 담당자 찾기
        //userRepository.findById(managerSaveRequest.getManagerUserId()): 새로운 담당자를 찾고,
        //만약 그 사람이 없으면 오류를 발생시켜요.
        User managerUser = userRepository.findById(managerSaveRequest.getManagerUserId())
                .orElseThrow(() -> new InvalidRequestException("등록하려고 하는 담당자 유저가 존재하지 않습니다!"));

        //작성자가 자기 자신을 담당자로 등록하려고 하면 오류
        //if (ObjectUtils.nullSafeEquals(...)): 작성자는 본인을 담당자로 등록할 수 없어요.
        // 그래서 이 부분에서 확인하고 오류를 던져요.
        if (ObjectUtils.nullSafeEquals(user.getId(), managerUser.getId())) {
            throw new InvalidRequestException("일정 작성자는 본인을 담당자로 등록할 수 없습니다!");
        }
        //새로운 담당자 등록하기
        //Manager newManagerUser = new Manager(managerUser, todo);: 새로 찾은 사람을 담당자로 일정에 등록해요.
        //managerRepository.save(newManagerUser);: 등록한 담당자를 저장해요.
        Manager newManagerUser = new Manager(managerUser, todo);
        Manager savedManagerUser = managerRepository.save(newManagerUser);

        return new ManagerSaveResponse(
                savedManagerUser.getId(),
                new UserResponse(managerUser.getId(), managerUser.getEmail())
        );
    }
    //담당자 리스트 보여주는 함수: getManagers
    //이 함수는 어떤 일정에 누가 담당자로 등록되어 있는지 모두 보여주는 함수예요.
    //일정 찾기
    //먼저 일정 번호(todoId)로 그 일정을 찾고, 없으면 오류를 던져요.
    //담당자 리스트 가져오기
    //그 일정에 등록된 모든 담당자를 리스트로 가져와요. (리스트는 여러 개의 데이터를 모아놓은 것)
    //담당자 정보 준비
    //각 담당자의 정보를 하나하나 꺼내서 이름과 이메일을 확인해요.
    public List<ManagerResponse> getManagers(long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        List<Manager> managerList = managerRepository.findByTodoIdWithUser(todo.getId());

        List<ManagerResponse> dtoList = new ArrayList<>();
        for (Manager manager : managerList) {
            User user = manager.getUser();
            dtoList.add(new ManagerResponse(
                    manager.getId(),
                    new UserResponse(user.getId(), user.getEmail())
            ));
        }
        return dtoList;
    }
    //담당자 삭제하는 함수: deleteManager
    //이 함수는 일정에서 담당자를 삭제하는 역할을 해요.
    //작성자 확인
    //이 부분도 먼저, 일정을 작성한 사람인지 확인해요. 작성자가 아니면 오류를 던져요.
    //해당 담당자가 진짜 맞는지 확인
    //삭제하려는 담당자가 이 일정에 맞게 등록된 사람이 맞는지 확인해요. 아니면 오류를 던져요.
    //담당자 삭제
    //담당자가 맞다면 그 담당자를 일정에서 삭제해요.
    @Transactional
    public void deleteManager(long userId, long todoId, long managerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new InvalidRequestException("해당 일정을 만든 유저가 유효하지 않습니다!");
        }

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new InvalidRequestException("Manager not found"));

        if (!ObjectUtils.nullSafeEquals(todo.getId(), manager.getTodo().getId())) {
            throw new InvalidRequestException("해당 일정에 등록된 담당자가 아닙니다!");
        }

        managerRepository.delete(manager);
    }
}

//요약하면
//담당자를 추가할 때: "일정을 작성한 사람이 맞는지"와 "담당자로 등록할 사람이 존재하는지" 확인하고 등록해요.
//담당자를 볼 때: 어떤 일정에 등록된 모든 담당자를 볼 수 있어요.
//담당자를 삭제할 때: "작성자가 맞는지", "삭제할 담당자가 맞는지" 확인하고 삭제해요.