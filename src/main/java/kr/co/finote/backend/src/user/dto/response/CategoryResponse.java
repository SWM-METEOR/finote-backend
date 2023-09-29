package kr.co.finote.backend.src.user.dto.response;

import kr.co.finote.backend.src.user.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private Long totalArticles;

    public static CategoryResponse of(Category category, Long totalArticles) {
        return new CategoryResponse(category.getId(), category.getName(), totalArticles);
    }
}
