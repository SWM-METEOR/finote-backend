package kr.co.finote.backend.src.blog.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.src.blog.domain.UsersBlog;
import kr.co.finote.backend.src.blog.repository.UsersBlogRepository;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersBlogService {

    private final UsersBlogRepository usersBlogRepository;

    public boolean duplicateBlogName(String blogName) {
        return usersBlogRepository.existsByName(blogName);
    }

    public boolean duplicateBlogUrl(String url) {
        return usersBlogRepository.existsByUrl(url);
    }

    public UsersBlog getUsersBlog(User user) {
        return usersBlogRepository
                .findByUserAndIsDeleted(user, false)
                .orElseThrow(() -> new CustomException(ResponseCode.BLOG_NOT_FOUND));
    }
}
