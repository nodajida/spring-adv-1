package com.sparta.springadv.domain.comment.service;

import com.sparta.springadv.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service: 이 클래스가 서비스 클래스라는 것을 알려줘요. 서비스 클래스는 비즈니스 로직을 처리하는 곳이에요.
//@RequiredArgsConstructor: 이 어노테이션은 final로 선언된 필드를 가진 생성자를 자동으로 만들어줘요.
// 그래서 클래스가 필요로 하는 필드를 생성자 주입을 통해 자동으로 주입받을 수 있어요.
@Service
@RequiredArgsConstructor
//서비스 클래스: CommentAdminService
//이 클래스는 댓글 관리와 관련된 일을 처리해요. 여기서는 댓글을 삭제하는 기능만 있어요.
public class CommentAdminService {
    //CommentRepository: 이 도구는 댓글 정보를 데이터베이스에서 찾거나 저장하는 역할을 해요.
    // 이 필드는 댓글 관련 작업을 하기 위해 필요해요.
    private final CommentRepository commentRepository;
    //댓글 삭제 메서드: deleteComment
    //@Transactional: 이 어노테이션은 이 메서드가 트랜잭션으로 실행된다는 것을 의미해요.
    // 트랜잭션은 데이터베이스 작업을 안전하게 처리하는 방법이에요. 즉, 댓글 삭제가 성공적으로 완료될 때까지 모든 작업을 안전하게 처리해요.
    //public void deleteComment(long commentId): 이 메서드는 댓글을 삭제하는 기능을 해요. commentId는 삭제하려는
    // 댓글의 고유 ID에요.
    //commentRepository.deleteById(commentId): 이 부분은 댓글의 ID를 사용해서 데이터베이스에서 댓글을 삭제해요.
    @Transactional
    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }
}
//요약하면
//이 서비스는 댓글을 삭제할 때 사용하는 간단한 기능을 제공해요. 댓글의 ID를 통해 댓글을 데이터베이스에서 제거하는 역할을 해요.
//이렇게 코드가 댓글 삭제 작업을 깔끔하게 처리해주는 역할을 해요!