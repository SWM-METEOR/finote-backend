package kr.co.finote.backend.src.user.dto.response;

import kr.co.finote.backend.src.user.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
