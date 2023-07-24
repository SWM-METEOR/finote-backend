package kr.co.finote.backend.src.user.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.AdditionalInfoRequest;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void validateNickname(String nickname) {
        boolean existsByNickname = userRepository.existsByNicknameAndIsDeleted(nickname, false);
        if (existsByNickname) {
            throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);
        } else if (nickname.length() > 100) {
            throw new CustomException(ResponseCode.NICKNAME_TOO_LONG);
        } // 그 외 금지문자 포함 등의 error 처리 예정
    }

    public void validateBlogName(String blogName) {
        boolean existsByBlogName = userRepository.existsByBlogNameAndIsDeleted(blogName, false);
        if (existsByBlogName) {
            throw new CustomException(ResponseCode.DUPLICATE_BLOG_NAME);
        } else if (blogName.length() > 100) {
            throw new CustomException(ResponseCode.BLOG_NAME_TOO_LONG);
        } // 그 외 금지문자 포함 등의 error 처리 예정
    }

    public void validateBlogUrl(String blogUrl) {
        boolean existsByBlogUrl = userRepository.existsByBlogUrlAndIsDeleted(blogUrl, false);
        if (existsByBlogUrl) {
            throw new CustomException(ResponseCode.DUPLICATE_BLOG_URL);
        } else if (blogUrl.length() > 100) {
            throw new CustomException(ResponseCode.BLOG_URL_TOO_LONG);
        } // 그 외 금지문자 포함 등의 error 처리 예정
    }

    @Transactional
    public void editAdditionalInfo(User user, AdditionalInfoRequest infoRequest) {
        validateNickname(infoRequest.getNickname());
        validateBlogName(infoRequest.getBlogName());
        validateBlogUrl(infoRequest.getBlogUrl());

        User findUser =
                userRepository
                        .findById(user.getId())
                        .orElseThrow(() -> new CustomException(ResponseCode.UNAUTHENTICATED));

        findUser.setNickname(infoRequest.getNickname());
        findUser.setBlogName(infoRequest.getBlogName());
        findUser.setBlogUrl(infoRequest.getBlogUrl());
    }
}
