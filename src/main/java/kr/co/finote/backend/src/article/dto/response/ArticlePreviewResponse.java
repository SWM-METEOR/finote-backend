package kr.co.finote.backend.src.article.dto.response;

import java.time.format.DateTimeFormatter;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import kr.co.finote.backend.src.article.domain.Article;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ArticlePreviewResponse {

    private Long id;
    private String title;
    private String body;
    private int totalLike;
    private int reply;
    private String authorNickname;
    private String date;
    private String thumbnail;
    private String profileImageUrl;

    public static ArticlePreviewResponse of(Article article, String previewBody) {
        ArticlePreviewResponse articlePreviewResponse =
                ArticlePreviewResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .totalLike(article.getTotalLike())
                        .reply(article.getTotalReply())
                        .authorNickname(article.getUser().getNickname())
                        .date(article.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .thumbnail(article.getThumbnail())
                        .profileImageUrl(article.getUser().getProfileImageUrl())
                        .build();
        articlePreviewResponse.updateBody(previewBody);
        return articlePreviewResponse;
    }

    public static ArticlePreviewResponse of(ArticleDocument document, String previewBody) {
        ArticlePreviewResponse articlePreviewResponse =
                ArticlePreviewResponse.builder()
                        .id(document.getArticleId())
                        .title(document.getTitle())
                        .totalLike(document.getTotalLike())
                        .reply(document.getTotalReply())
                        .authorNickname(document.getAuthorNickname())
                        .date(document.getCreatedDate())
                        .thumbnail(document.getThumbnail() == null ? "" : document.getThumbnail())
                        .profileImageUrl(
                                "https://finote-image-bucket.s3.ap-northeast-2.amazonaws.com/profile_image.png")
                        .build();
        articlePreviewResponse.updateBody(previewBody);
        return articlePreviewResponse;
    }

    // 도메인 안에서만 호출할 수 있도록 private
    private void updateBody(String previewBody) {
        this.body = previewBody;
    }
}
