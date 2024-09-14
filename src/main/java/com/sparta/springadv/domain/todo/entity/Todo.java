package com.sparta.springadv.domain.todo.entity;

import com.sparta.springadv.domain.comment.entity.Comment;
import com.sparta.springadv.domain.common.entity.Timestamped;
import com.sparta.springadv.domain.manager.entity.Manager;
import com.sparta.springadv.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;



import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "todos")
public class Todo extends Timestamped {

    //@Id: 이 필드가 이 객체를 구분하는 ID임을 나타내요.
    //@GeneratedValue(strategy = GenerationType.IDENTITY): ID 값이 자동으로 생성되도록 해요.
    //private Long id: 이 객체의 고유 ID를 저장해요.
    //private String title: 할 일의 제목을 저장해요.
    //private String contents: 할 일의 내용을 저장해요.
    //private String weather: 날씨를 저장해요.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
    private String weather;

    //@ManyToOne(fetch = FetchType.LAZY): 여러 Todo가 하나의 User에 속해요.
    // 데이터는 필요할 때만 가져와요.
    //@JoinColumn(name = "user_id", nullable = false): user_id라는 컬럼으로 User와 연결돼요.
    // User는 필수로 있어야 해요.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //@OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE): 하나의 Todo가 여러 개의 Comment와 연결돼요.
    // Todo가 삭제되면 관련된 Comment도 삭제돼요.
    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    //@OneToMany(mappedBy = "todo", cascade = CascadeType.PERSIST): 하나의 Todo가 여러 개의 Manager와 연결돼요.
    // Todo가 저장되면 관련된 Manager도 저장돼요.
    @OneToMany(mappedBy = "todo", cascade = CascadeType.PERSIST)
    private List<Manager> managers = new ArrayList<>();

    //생성자
    //Todo 객체를 만들 때 필요한 정보들을 설정해요. 생성자에 주어진 정보를 이용해 title, contents, weather, user를 설정하고,
    // managers 리스트에 새로운 Manager를 추가해요.
    public Todo(String title, String contents, String weather, User user) {
        this.title = title;
        this.contents = contents;
        this.weather = weather;
        this.user = user;
        this.managers.add(new Manager(user, this));
    }
    //업데이트 메서드
    //Todo의 제목과 내용을 수정할 수 있어요.
    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
//정리
//이 클래스는 할 일을 저장하는 객체예요.
//이 객체는 ID, 제목, 내용, 날씨를 저장하고, 여러 개의 댓글과 관리자와 연결돼요.
//생성자는 새로운 Todo를 만들 때 필요한 정보를 설정하고, 업데이트 메서드는 제목과 내용을 수정해요.
