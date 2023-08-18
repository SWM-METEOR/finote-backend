package kr.co.finote.backend.src.article.domain;

import javax.persistence.*;
import kr.co.finote.backend.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    public static ArticleKeyword createArticleKeyword(Article article, Keyword keyword) {
        return ArticleKeyword.builder().article(article).keyword(keyword).build();
    }
}
