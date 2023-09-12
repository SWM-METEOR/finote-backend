package kr.co.finote.backend.src.qna.domain;

import javax.persistence.*;
import kr.co.finote.backend.global.entity.BaseEntity;
import kr.co.finote.backend.src.qna.dto.request.PostQuestionRequest;
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
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title = "";

    private String body = "";

    @ColumnDefault("0")
    private int totalLike;

    @ColumnDefault("0")
    private int totalAnswer;

    public static Question createQuestion(User user, PostQuestionRequest request) {
        return Question.builder()
                .user(user)
                .title(request.getTitle())
                .body(request.getBody())
                .totalLike(0)
                .totalAnswer(0)
                .build();
    }

    public void edit(PostQuestionRequest request) {
        this.title = request.getTitle().trim();
        this.body = request.getBody();
    }
}
