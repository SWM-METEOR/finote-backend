package kr.co.finote.backend.src.user.service;

import java.util.List;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.AdditionalInfoRequest;
import kr.co.finote.backend.src.user.dto.request.UserArticlesRequest;
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
            throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);
        } else if (nickname.length() > NICK_NAME_MAX_LENGTH) {
            throw new CustomException(ResponseCode.NICKNAME_TOO_LONG);
        } // 그 외 금지문자 포함 등의 error 처리 예정
    }

    public void validateBlogName(String blogName) {
        boolean existsByBlogName = userRepository.existsByBlogNameAndIsDeleted(blogName, false);
        if (existsByBlogName) {
            throw new CustomException(ResponseCode.DUPLICATE_BLOG_NAME);
        } else if (blogName.length() > BLOG_NAME_MAX_LENGTH) {
            throw new CustomException(ResponseCode.BLOG_NAME_TOO_LONG);
        } // 그 외 금지문자 포함 등의 error 처리 예정
    }

    public void validateBlogUrl(String blogUrl) {
        boolean existsByBlogUrl = userRepository.existsByBlogUrlAndIsDeleted(blogUrl, false);
        if (existsByBlogUrl) {
            throw new CustomException(ResponseCode.DUPLICATE_BLOG_URL);
        } else if (blogUrl.length() > BLOG_URL_MAX_LENGTH) {
            throw new CustomException(ResponseCode.BLOG_URL_TOO_LONG);
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
                        .orElseThrow(() -> new CustomException(ResponseCode.UNAUTHENTICATED));

        findUser.updateAdditionalInfo(request);
    }

    public UserArticlesRequest findArticlesAll(User user, int page, int size) {
        int pageNum = page - 1;
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by("createdDate").descending());

        Page<Article> result = articleRepository.findByUserAndIsDeleted(user, false, pageable);
        List<Article> content = result.getContent();

        return UserArticlesRequest.of(pageNum, size, content);
    }
}
