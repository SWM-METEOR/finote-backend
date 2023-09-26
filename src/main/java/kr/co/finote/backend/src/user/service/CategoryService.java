package kr.co.finote.backend.src.user.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.src.user.domain.Category;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.CategoryRequest;
import kr.co.finote.backend.src.user.dto.response.CategoryResponse;
import kr.co.finote.backend.src.user.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse addCategory(User loginUser, CategoryRequest request) {
        isDuplicate(loginUser, request);
        Category category = Category.createCategory(loginUser, request.getName());
        Category saveCategory = categoryRepository.save(category);
        return CategoryResponse.of(saveCategory);
    }

    private void isDuplicate(User loginUser, CategoryRequest request) {
        boolean exists =
                categoryRepository.existsByNameAndUserAndIsDeleted(request.getName(), loginUser, false);
        if (exists) {
            throw new InvalidInputException(ResponseCode.CATEGORY_ALREADY_EXIST);
        }
    }
}
