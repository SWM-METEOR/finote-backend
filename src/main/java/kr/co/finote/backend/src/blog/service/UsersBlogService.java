package kr.co.finote.backend.src.blog.service;

import kr.co.finote.backend.src.blog.repository.UsersBlogRepository;
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
}
