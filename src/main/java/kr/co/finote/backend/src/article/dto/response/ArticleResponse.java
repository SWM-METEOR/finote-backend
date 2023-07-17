package kr.co.finote.backend.src.article.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArticleResponse {

    private String id;
    private String title;
    private String body;
    private String createDate;
    private String authorId;
    private String authorNickname;
    private String profileImageUrl;
}
