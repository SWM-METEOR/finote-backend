package kr.co.finote.backend.src.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.article.service.ArticleService;
import kr.co.finote.backend.src.user.domain.Category;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.CategoryRequest;
import kr.co.finote.backend.src.user.dto.response.CategoryListResponse;
import kr.co.finote.backend.src.user.dto.response.CategoryResponse;
import kr.co.finote.backend.src.user.dto.response.PostCategoryResponse;
import kr.co.finote.backend.src.user.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final ArticleService articleService;

    @Transactional
    public PostCategoryResponse addCategory(User loginUser, CategoryRequest request) {
        isDuplicate(loginUser, request);
        Category category = Category.createCategory(loginUser, request.getName());
        Category saveCategory = categoryRepository.save(category);
        return PostCategoryResponse.of(saveCategory);
    }

    private void isDuplicate(User loginUser, CategoryRequest request) {
        boolean exists =
                categoryRepository.existsByNameAndUserAndIsDeleted(request.getName(), loginUser, false);
        if (exists) {
            throw new InvalidInputException(ResponseCode.CATEGORY_ALREADY_EXIST);
        }
    }

    @Transactional
    public PostCategoryResponse editCategory(
            User loginUser, Long categoryId, CategoryRequest request) {
        Category category = findById(categoryId);
        checkCategoryAuthority(loginUser, category);
        category.editCategory(request.getName());
        return PostCategoryResponse.of(category);
    }

    @Transactional
    public void deleteCategory(User loginUser, Long categoryId) {
        Category category = findById(categoryId);
        checkCategoryAuthority(loginUser, category);
        checkCategoryIsEmpty(category); // 카테고리에 글이 존재할 경우 삭제하지 못함
        category.deleteCategory();
    }

    private Category findById(Long categoryId) {
        return categoryRepository
                .findByIdAndIsDeleted(categoryId, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.CATEGORY_NOT_FOUND));
    }

    private void checkCategoryIsEmpty(Category category) {
        if (articleService.existsByCategory(category))
            throw new InvalidInputException(ResponseCode.CATEGORY_NOT_EMPTY);
    }

    private static void checkCategoryAuthority(User loginUser, Category category) {
        if (!loginUser.getEmail().equals(category.getUser().getEmail())) {
            throw new InvalidInputException(ResponseCode.CATEGORY_NOT_WRITER);
        }
    }

    public CategoryListResponse getCategories(User loginUser) {
        List<CategoryResponse> categoryResponseList =
                getEtcCategoryInformation(loginUser); // 기타 카테고리 정보
        getCategoriesInformation(loginUser, categoryResponseList); // 그 외 유저 생성한 카테고리 정보
        return CategoryListResponse.createCategoryListResponse(categoryResponseList);
    }

    @NotNull
    private List<CategoryResponse> getEtcCategoryInformation(User loginUser) {
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        Category etcCategory =
                categoryRepository
                        .findByNameAndIsDeleted("기타", false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.CATEGORY_NOT_FOUND));
        Long etcCategoryTotalArticles = getTotalArticles(etcCategory, loginUser); // 기타 카테고리에 존재하는 글 개수
        CategoryResponse etcCategoryResponse =
                CategoryResponse.of(etcCategory, etcCategoryTotalArticles);
        categoryResponseList.add(etcCategoryResponse);
        return categoryResponseList;
    }

    private void getCategoriesInformation(
            User loginUser, List<CategoryResponse> categoryResponseList) {
        List<Category> categoryList =
                categoryRepository.findAllByUserAndIsDeletedOrderByCreatedDate(loginUser, false);
        List<CategoryResponse> customCategoryList =
                categoryList.stream()
                        .map(
                                category -> {
                                    Long totalArticles = getTotalArticles(category, loginUser);
                                    return CategoryResponse.of(category, totalArticles);
                                })
                        .collect(Collectors.toList());
        categoryResponseList.addAll(customCategoryList);
    }

    private Long getTotalArticles(Category category, User loginUser) {
        return articleService.countByCategoryAndUser(category, loginUser);
    }
}
