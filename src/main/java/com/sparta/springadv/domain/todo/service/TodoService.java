package com.sparta.springadv.domain.todo.service;

import com.sparta.springadv.client.WeatherClient;
import com.sparta.springadv.domain.common.dto.AuthUser;
import com.sparta.springadv.domain.common.exception.InvalidRequestException;
import com.sparta.springadv.domain.todo.dto.TodoResponse;
import com.sparta.springadv.domain.todo.dto.TodoSaveRequest;
import com.sparta.springadv.domain.todo.dto.TodoSaveResponse;
import com.sparta.springadv.domain.todo.entity.Todo;
import com.sparta.springadv.domain.todo.repository.TodoRepository;
import com.sparta.springadv.domain.user.dto.UserResponse;
import com.sparta.springadv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    //클래스 멤버 변수들: todoRepository, weatherClient
    //todoRepository: 이건 할 일을 저장하거나 가져올 때 사용하는 도구예요. 저장된 할 일들을 관리해줘요.
    //weatherClient: 이건 오늘의 날씨를 가져오는 도구 날씨 정보를 받아오면, 할 일을 저장할 때 함께 사용
    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional

    //public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest)
    //이건 함수의 이름과 재료야. 이 함수는 할 일을 저장하고, 그 결과를 돌려주는 역할을 해.
    //AuthUser authUser: 누가 할 일을 만들었는지 알려주는 정보야.
    //TodoSaveRequest todoSaveRequest: 만들고 싶은 할 일의 제목과 내용을 담은 상자야.
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {

    //  User user = User.fromAuthUser(authUser);여기서는 authUser 정보를 바탕으로 그 사람이 누구인지를 확인해.
    // 마치 네가 이름표를 내밀면 선생님이 "아, 이 친구구나!"라고 확인하는 것과 같아.
        User user = User.fromAuthUser(authUser);

    //이건 날씨 정보를 가져오는 부분이야. "오늘 날씨가 어때요?" 하고 물어서, 날씨를 가져오는 거지.
        String weather = weatherClient.getTodayWeather();

    //이제 새로 만들 '할 일'을 준비하는 단계
    //todoSaveRequest.getTitle(): 할 일의 제목.
    //todoSaveRequest.getContents(): 할 일의 내용.
    //weather: 오늘의 날씨.
    //user: 이 할 일을 만든 사람(누군지 이미 확인한 'user')
        //이걸로 새 할 일(newTodo)이 만들어진 거야!
        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );

    //이제 새로 만든 할 일을 todoRepository를 사용해서 데이터베이스에 저장해. 이걸 통해 'savedTodo'라는 이름으로 할 일이
    // 잘 저장됐다는 결과를 받아와.
        Todo savedTodo = todoRepository.save(newTodo);

    //마지막으로 할 일이 잘 저장됐다는 걸 알려주는 부분이야. 저장한 할 일의 정보
    // (아이디, 제목, 내용, 날씨, 사용자의 정보)를 다시 돌려주는 거지.
        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    //'할 일' 목록을 가져오는 코드
    // public Page<TodoResponse> getTodos(int page, int size)
    //이 부분은 getTodos라는 함수의 이름이야. 이 함수는 '할 일' 목록을 여러 개 가져오는 역할을 해.
    //int page: 가져올 페이지 번호야. 쉽게 말하면, 책에서 몇 번째 페이지를 보고 싶은지 정하는 거야.
    //int size: 한 페이지에 몇 개의 할 일을 보여줄지 정해. 예를 들어, 한 페이지에 5개나 10개의 할 일을 보여줄 수 있어.
    //Page<TodoResponse>는 함수가 나중에 돌려줄 값이야. 이건 여러 개의 할 일 정보를 담고 있는 상자라고 생각하면 돼.
    public Page<TodoResponse> getTodos(int page, int size) {

        //Pageable pageable = PageRequest.of(page - 1, size);
        //여기서는 몇 번째 페이지에서 몇 개의 할 일을 가져올지 정해.
        //page - 1: 페이지는 보통 0부터 시작해서, 입력한 페이지 번호에서 1을 빼는 거야.
        //예를 들어, 첫 페이지를 보고 싶으면 page = 1이니까, 0번째 페이지를 가져오게 되는 거야.
        //size: 한 페이지에 몇 개의 할 일을 가져올지 설정하는 값이야.
        //이렇게 해서 pageable이라는 상자에 페이지와 크기 정보를 담아.
        Pageable pageable = PageRequest.of(page - 1, size);

        // Page<Todo> todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable);
        // 이 부분은 실제로 '할 일'들을 가져오는 단계야.
        // todoRepository: 할 일을 저장하고, 찾아주는 도우미야.
        // findAllByOrderByModifiedAtDesc(pageable): 이건 모든 할 일들을 최근에 수정된 순서대로 가져오는 명령이야. 가장 최신에 수정된 할 일부터 차례대로 가져오는 거지.
        // 결과로 여러 개의 '할 일'이 todos라는 상자에 담겨 있어.
        Page<Todo> todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable);

        //  return todos.map(todo -> new TodoResponse(...));
        // 이제 할 일 목록을 TodoResponse라는 형태로 바꿔서 돌려주는 단계야. 각 할 일마다 필요한 정보를 꺼내서, 새로운 'TodoResponse'라는 상자에 담아 돌려줘.
        return todos.map(todo -> new TodoResponse(

        //new TodoResponse(...)
        //여기서는 각각의 '할 일'에서 필요한 정보를 하나씩 꺼내서 TodoResponse에 담아
        //마치 각 학생의 이름, 나이,학급 정보를 기록하는 것처럼, 여기서는 '할 일'의 여러 정보를 기록해.
        // todo.getId(): 할 일의 아이디.
        // todo.getTitle(): 할 일의 제목.
        //  todo.getContents(): 할 일의 내용.
        //  todo.getWeather(): 할 일을 만든 날의 날씨.
        // new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()): 이 할 일을 만든 사용자의 정보.
        // odo.getCreatedAt(): 할 일을 처음 만든 날짜.
        //  todo.getModifiedAt(): 할 일이 마지막으로 수정된 날짜.
        //  이렇게 해서, 여러 개의 할 일을 TodoResponse라는 형태로 바꿔서 우리에게 돌려줘.
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

        //public TodoResponse getTodo(long todoId)
        //이 부분은 getTodo라는 함수의 이름이야. 이 함수는 '할 일' 하나를 가져오는 역할을 해.
        //long todoId: 어떤 '할 일'을 가져올지 결정하는 숫자야. 각 할 일은 고유한 번호를 가지고 있는데,
        // 그 번호를 todoId라고 불러. 이 번호를 통해 그 할 일을 찾는 거야.
        //TodoResponse: 함수가 나중에 돌려줄 값이야. '할 일'의 정보를 담아서 TodoResponse라는 상자에 넣고 돌려줘.
    public TodoResponse getTodo(long todoId) {

        //Todo todo = todoRepository.findByIdWithUser(todoId)
        // 여기서는 'todoId'로 특정한 할 일을 찾는 거야.
        // todoRepository: 할 일을 데이터베이스에서 꺼내오거나 저장해주는 도우미야.
        // findByIdWithUser(todoId): 이건 데이터베이스에서 'todoId'와 연결된 할 일을 찾는 명령이야. 이 명령은 그 할 일을 만든 사용자 정보까지 같이 찾아줘.
        Todo todo = todoRepository.findByIdWithUser(todoId)

                // .orElseThrow(() -> new InvalidRequestException("Todo not found"))
                //이건 혹시 'todoId'로 찾은 할 일이 없을 때 대비한 부분이야. 만약 '할 일'을 찾지 못하면, 에러를 내보내고
                // "Todo not found"라고 알려주는 거야. 즉, "찾고 있는 할 일이 없어!"라고 말하는 거지.
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));
        //User user = todo.getUser();
        //이 부분은 찾은 할 일의 주인(사용자)이 누구인지 확인하는 거야.
        // 'todo.getUser()'를 통해 '할 일'을 만든 사람이 누구인지 가져와서 user라는 이름으로 저장해.
        User user = todo.getUser();

        //return new TodoResponse(...)
        //여기서는 찾은 '할 일'의 정보를 꺼내서 TodoResponse라는 상자에 담아 돌려주는 단계야.
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

}
