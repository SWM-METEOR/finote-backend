package kr.co.finote.backend.src.user.dto.response;

import kr.co.finote.backend.src.user.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCategoryResponse {

    private Long id;
    private String name;

    public static PostCategoryResponse of(Category category) {
        return new PostCategoryResponse(category.getId(), category.getName());
    }
}
