package kr.co.finote.backend.src.user.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryListResponse {

    private List<CategoryResponse> categoryResponseList;

    public static CategoryListResponse createCategoryListResponse(
            List<CategoryResponse> categoryResponseList) {
        return new CategoryListResponse(categoryResponseList);
    }
}
