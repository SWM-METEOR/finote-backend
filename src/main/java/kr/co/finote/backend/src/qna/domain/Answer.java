package kr.co.finote.backend.src.qna.domain;

import javax.persistence.*;
import kr.co.finote.backend.global.entity.BaseEntity;
import kr.co.finote.backend.src.qna.dto.request.PostAnswerRequest;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private String body;

    @ColumnDefault("0")
    private int totalLike;

    @ColumnDefault("0")
    private int totalUnlike;

    public static Answer createAnswer(User user, Question question, PostAnswerRequest request) {
        return Answer.builder().user(user).question(question).body(request.getBody()).build();
    }
}
