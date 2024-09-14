package com.sparta.springadv.domain.manager.entity;

import com.sparta.springadv.domain.todo.entity.Todo;
import com.sparta.springadv.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "managers")
public class Manager {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //user는 이 일정을 만든 사람을 가리킨다. 일정은 여러 개 있을 수 있지만, 한 사람은 여러 일정을 만들 수 있다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 일정 만든 사람 id
    private User user;
    //todo는 이 일정(일)을 가리킨다. 하나의 일정(일)을 여러 사람이 관리할 수 있지만, 하나의 일정은 하나의 고유번호(todo_id)를 가진다.
    @ManyToOne(fetch = FetchType.LAZY) // 일정 id
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;
    //이 부분은 Manager라는 클래스를 만들 때, user(일정을 만든 사람)와
    // todo(일정) 정보를 입력받아 객체를 만들어준다.
    public Manager(User user, Todo todo) {
        this.user = user;
        this.todo = todo;
    }
}

