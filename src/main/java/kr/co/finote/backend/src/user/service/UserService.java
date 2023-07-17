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

    public boolean duplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean duplicateBlogName(String blogName) {
        return userRepository.existsByBlogName(blogName);
    }

    public boolean duplicateBlogUrl(String url) {
        return userRepository.existsByBlogUrl(url);
    }

    @Transactional
    public void editAdditionalInfo(User user, AdditionalInfoRequest infoRequest) {
        if (duplicateNickname(infoRequest.getNickname())) {
            throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);
        }
        if (duplicateBlogUrl(infoRequest.getBlogUrl())) {
            throw new CustomException(ResponseCode.DUPLICATE_BLOG_URL);
        }

        if (duplicateBlogName(infoRequest.getBlogName())) {
            throw new CustomException(ResponseCode.DUPLICATE_BLOG_NAME);
        }

        User findUser =
                userRepository
                        .findById(user.getId())
                        .orElseThrow(() -> new CustomException(ResponseCode.UNAUTHENTICATED));

        findUser.setNickname(infoRequest.getNickname());
        findUser.setBlogName(infoRequest.getBlogName());
        findUser.setBlogUrl(infoRequest.getBlogUrl());
    }
}
