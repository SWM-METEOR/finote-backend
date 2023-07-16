package kr.co.finote.backend.src.user.service;

import kr.co.finote.backend.src.blog.domain.UsersBlog;
import kr.co.finote.backend.src.blog.dto.response.BlogResponse;
import kr.co.finote.backend.src.blog.service.UsersBlogService;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UsersBlogService usersBlogService;

    public boolean duplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public BlogResponse getBlogInfo(User loginUser) {
        UsersBlog findBlog = usersBlogService.getUsersBlog(loginUser);

        return new BlogResponse(findBlog.getName(), findBlog.getUrl());
    }
}
