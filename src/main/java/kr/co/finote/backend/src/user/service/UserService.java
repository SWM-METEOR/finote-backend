package kr.co.finote.backend.src.user.service;

import java.util.List;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.global.exception.UnAuthorizedException;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewListResponse;
import kr.co.finote.backend.src.article.dto.response.ArticlePreviewResponse;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
import kr.co.finote.backend.src.article.utils.ArticlePreviewUtils;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.AdditionalInfoRequest;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    public static final int NICK_NAME_MAX_LENGTH = 100;
    public static final int BLOG_NAME_MAX_LENGTH = 100;
    public static final int BLOG_URL_MAX_LENGTH = 100;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public void validateNickname(String nickname) {
        boolean existsByNickname = userRepository.existsByNicknameAndIsDeleted(nickname, false);
        if (existsByNickname) {
            throw new InvalidInputException(ResponseCode.DUPLICATE_NICKNAME);
        } else if (nickname.length() > NICK_NAME_MAX_LENGTH) {
            throw new InvalidInputException(ResponseCode.NICKNAME_TOO_LONG);
        } // 그 외 금지문자 포함 등의 error 처리 예정
    }

    public void validateBlogName(String blogName) {
        boolean existsByBlogName = userRepository.existsByBlogNameAndIsDeleted(blogName, false);
        if (existsByBlogName) {
            throw new InvalidInputException(ResponseCode.DUPLICATE_BLOG_NAME);
        } else if (blogName.length() > BLOG_NAME_MAX_LENGTH) {
            throw new InvalidInputException(ResponseCode.BLOG_NAME_TOO_LONG);
        } // 그 외 금지문자 포함 등의 error 처리 예정
    }

    public void validateBlogUrl(String blogUrl) {
        boolean existsByBlogUrl = userRepository.existsByBlogUrlAndIsDeleted(blogUrl, false);
        if (existsByBlogUrl) {
            throw new InvalidInputException(ResponseCode.DUPLICATE_BLOG_URL);
        } else if (blogUrl.length() > BLOG_URL_MAX_LENGTH) {
            throw new InvalidInputException(ResponseCode.BLOG_URL_TOO_LONG);
        } // 그 외 금지문자 포함 등의 error 처리 예정
    }

    @Transactional
    public void editAdditionalInfo(User user, AdditionalInfoRequest request) {
        validateNickname(request.getNickname());
        validateBlogName(request.getBlogName());
        validateBlogUrl(request.getBlogUrl());

        User findUser =
                userRepository
                        .findByIdAndIsDeleted(user.getId(), false)
                        .orElseThrow(() -> new UnAuthorizedException(ResponseCode.UNAUTHENTICATED));

        findUser.updateAdditionalInfo(request);
    }

    public ArticlePreviewListResponse findArticlesAll(User user, int page, int size) {
        int pageNum = page - 1;
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by("createdDate").descending());

        Page<Article> result = articleRepository.findByUserAndIsDeleted(user, false, pageable);
        List<Article> contents = result.getContent();

        List<ArticlePreviewResponse> articlePreviewResponseList =
                ArticlePreviewUtils.ToArticlesPreivewResponses(contents);

        return ArticlePreviewListResponse.of(pageNum, size, articlePreviewResponseList);
    }

    public User findById(String userId) {
        return userRepository
                .findByIdAndIsDeleted(userId, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.USER_NOT_FOUND));
    }
}
