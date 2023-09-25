package kr.co.finote.backend.src.qna.document;

import java.time.format.DateTimeFormatter;
import javax.persistence.Id;
import kr.co.finote.backend.src.qna.domain.Question;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "question")
@Getter
@AllArgsConstructor
@Builder
public class QuestionDocument {

    @Id private String id;

    private Long questionId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    private String authorNickname;
    private String profileImageUrl;
    private String createdDate;

    private int totalAnswer;

    public static QuestionDocument createQuestionDocument(Question question, User user) {
        return QuestionDocument.builder()
                .questionId(question.getId())
                .title(question.getTitle())
                .authorNickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .createdDate(user.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .totalAnswer(question.getTotalAnswer())
                .build();
    }

    public void edit(String title) {
        this.title = title;
    }
}
