package com.sparta.springadv.domain.comment.service;

import com.sparta.springadv.domain.comment.dto.CommentResponse;
import com.sparta.springadv.domain.comment.dto.CommentSaveRequest;
import com.sparta.springadv.domain.comment.dto.CommentSaveResponse;
import com.sparta.springadv.domain.comment.entity.Comment;
import com.sparta.springadv.domain.comment.repository.CommentRepository;
import com.sparta.springadv.domain.common.dto.AuthUser;
import com.sparta.springadv.domain.common.exception.InvalidRequestException;
import com.sparta.springadv.domain.todo.entity.Todo;
import com.sparta.springadv.domain.todo.repository.TodoRepository;
import com.sparta.springadv.domain.user.dto.UserResponse;
import com.sparta.springadv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
//서비스 클래스: CommentService
//이 클래스는 댓글과 관련된 기능을 처리해요. 댓글을 저장하고 조회하는 기능이 포함되어 있어요.
public class CommentService {

//TodoRepository: 이 도구는 일정(Todo) 정보를 데이터베이스에서 찾거나 저장하는 역할을 해요.
//CommentRepository: 이 도구는 댓글 정보를 데이터베이스에서 찾거나 저장하는 역할을 해요.
    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    @Transactional
    //댓글 저장 메서드: saveComment
    //AuthUser authUser: 현재 로그인한 사용자의 정보가 담겨 있어요.
    //long todoId: 댓글이 달릴 일정의 ID를 나타내요.
    //CommentSaveRequest commentSaveRequest: 댓글의 내용과 같은 정보를 담고 있는 요청 데이터에요.
    public CommentSaveResponse saveComment(AuthUser authUser, long todoId, CommentSaveRequest commentSaveRequest) {
        //사용자 정보 가져오기
        //User user = User.fromAuthUser(authUser);
        //로그인한 사용자의 정보를 User 객체로 변환해요.
        User user = User.fromAuthUser(authUser);
        //일정 정보 찾기
        //Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new InvalidRequestException("Todo not found"));
        //일정 ID를 통해 일정을 찾아요. 일정이 없으면 오류를 던져요.
        Todo todo = todoRepository.findById(todoId).orElseThrow(() ->
                new InvalidRequestException("Todo not found"));
        //새 댓글 생성
        //Comment newComment = new Comment(...)
        //댓글의 내용을 담아서 새 댓글을 생성해요.
        Comment newComment = new Comment(
                commentSaveRequest.getContents(),
                user,
                todo
        );
        //댓글 저장하기
        //Comment savedComment = commentRepository.save(newComment);
        //새로 생성한 댓글을 데이터베이스에 저장해요.
        Comment savedComment = commentRepository.save(newComment);
        //응답 준비하기
        //return new CommentSaveResponse(...)
        //저장된 댓글과 관련된 정보를 담아서 응답을 준비해요.
        return new CommentSaveResponse(
                savedComment.getId(),
                savedComment.getContents(),
                new UserResponse(user.getId(), user.getEmail())
        );
    }
    //댓글 조회 메서드: getComments
    //long todoId: 댓글을 조회할 일정의 ID를 나타내요.
    public List<CommentResponse> getComments(long todoId) {
        //메서드 내용
        //댓글 목록 가져오기
        //List<Comment> commentList = commentRepository.findByTodoIdWithUser(todoId);
        //일정 ID를 통해 댓글 목록을 가져와요.
        List<Comment> commentList = commentRepository.findByTodoIdWithUser(todoId);

        //댓글 정보를 준비하기
        //List<CommentResponse> dtoList = new ArrayList<>();
        //댓글 정보를 담을 리스트를 준비해요.
        List<CommentResponse> dtoList = new ArrayList<>();
        //각 댓글에 대해 처리하기
        //댓글 목록을 하나씩 꺼내서 CommentResponse 객체로 변환해요.
        //dtoList.add(dto);: 변환된 댓글 정보를 리스트에 추가해요.
        for (Comment comment : commentList) {
            User user = comment.getUser();
            CommentResponse dto = new CommentResponse(
                    comment.getId(),
                    comment.getContents(),
                    new UserResponse(user.getId(), user.getEmail())
            );

            dtoList.add(dto);
        }

       //댓글 목록 응답하기
       //return dtoList;
       //댓글 정보가 담긴 리스트를 반환해요.
        return dtoList;
    }
}
//요약
//댓글 저장: 댓글 내용을 받아서 일정에 저장하고, 저장된 댓글의 정보를 응답으로 보내줘요.
//댓글 조회: 특정 일정에 달린 모든 댓글을 조회하고, 댓글 정보를 담은 리스트를 응답으로 보내줘요.
//이 코드는 댓글을 추가하고 조회하는 기능을 제공하는 서비스예요!