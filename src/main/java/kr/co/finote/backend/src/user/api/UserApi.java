package kr.co.finote.backend.src.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewListResponse;
import kr.co.finote.backend.src.article.service.ArticleLikeService;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.*;
import kr.co.finote.backend.src.user.dto.response.*;
import kr.co.finote.backend.src.user.service.CategoryService;
import kr.co.finote.backend.src.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User API", description = "유저 API")
public class UserApi {

    UserService userService;
    ArticleLikeService articleLikeService;
    CategoryService categoryService;

    /* Email Join API */
    @Operation(summary = "이메일 인증 코드 발송")
    @PostMapping(value = "/issue/email-code")
    public void sendEmailCode(@RequestBody @Valid EmailCodeRequest request) {
        userService.issueEmailCode(request);
    }

    @Operation(summary = "이메일 인증 코드 확인")
    @PostMapping(value = "/validation/email-code")
    public EmailCodeValidationResponse validateEmailCode(
            @RequestBody @Valid EmailCodeValidationRequest request) {
        return userService.validateEmailCode(request);
    }

    @Operation(summary = "이메일 회원가입")
    @PostMapping(value = "/join/email")
    public void joinByEmail(@RequestBody @Valid EmailJoinRequest request) {
        userService.joinByEmail(request);
    }

    /* Field Validation API */
    @Operation(summary = "이메일 중복 검사")
    @PostMapping(value = "/validation/email")
    public ValidationEmailResponse validateEmail(@RequestBody EmailDuplicateCheckRequest request) {
        return userService.validateEmail(request.getEmail());
    }

    @Operation(summary = "닉네임 중복 검사")
    @PostMapping(value = "/validation/nickname")
    public ValidationNicknameResponse validateNickname(
            @RequestBody @Valid NicknameDuplicateCheckRequest request) {
        return userService.validateNickname(request.getNickname());
    }

    @Operation(summary = "블로그 이름 중복 검사")
    @PostMapping(value = "/validation/blog-name")
    public ValidationBlogNameResponse validateBlogName(
            @RequestBody @Valid BlogNameDuplicateCheckRequest request) {
        return userService.validateBlogName(request.getBlogName());
    }

    /* Field Getter API */
    @Operation(summary = "닉네임 가져오기")
    @GetMapping("/nickname")
    public NicknameResponse getNickname(@Login User loginUser) {
        return NicknameResponse.of(loginUser);
    }

    @Operation(summary = "블로그 정보 가져오기")
    @GetMapping("/blog-info")
    public BlogResponse getBlogInfo(@Login User loginUser) {
        return BlogResponse.of(loginUser);
    }

    @Operation(summary = "프로밀 이미지 url 가져오기")
    @GetMapping("/profile-image-url")
    public ProfileImageUrlResponse getProfileImage(@Login User loginUser) {
        return ProfileImageUrlResponse.of(loginUser);
    }

    /* API related to additional-info */
    @Operation(summary = "추가 정보 입력")
    @PostMapping("/additional-info")
    public void additionalInfo(
            @Login User loginUser, @RequestBody @Valid AdditionalInfoRequest request) {
        userService.editAdditionalInfo(loginUser, request);
    }

    @Operation(summary = "내가 좋아요 한 글의 갯수")
    @GetMapping("/articles/like/count")
    public LikeCountResponse getLikeCount(@Login User loginUser) {
        return articleLikeService.getLikeCount(loginUser);
    }

    @Operation(summary = "내가 좋아요 한 글")
    @GetMapping("/articles/like")
    public ArticlePreviewListResponse getLikeArticles(
            @Login User loginUser,
            @RequestParam int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return articleLikeService.getLikeArticles(loginUser, page, size);
    }

    /** category * */
    @Operation(summary = "카테고리 목록")
    @GetMapping("/categories")
    public CategoryListResponse getCategories(@Login User loginUser) {
        return categoryService.getCategories(loginUser);
    }

    @Operation(summary = "카테고리 생성")
    @PostMapping("/categories/add")
    public PostCategoryResponse addCategory(
            @Login User loginUser, @RequestBody @Valid CategoryRequest request) {
        return categoryService.addCategory(loginUser, request);
    }

    @Operation(summary = "카테고리 수정")
    @PostMapping("/categories/edit/{category-id}")
    public PostCategoryResponse editCategory(
            @Login User loginUser,
            @PathVariable("category-id") Long categoryId,
            @RequestBody @Valid CategoryRequest request) {
        return categoryService.editCategory(loginUser, categoryId, request);
    }

    @Operation(summary = "카테고리 삭제")
    @PostMapping("/categories/delete/{category-id}")
    public void deleteCategory(@Login User loginUser, @PathVariable("category-id") Long categoryId) {
        categoryService.deleteCategory(loginUser, categoryId);
    }
}
