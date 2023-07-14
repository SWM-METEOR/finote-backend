package kr.co.finote.backend.src.blog.api;

import static org.springframework.http.MediaType.*;

import java.util.HashMap;
import java.util.Map;
import kr.co.finote.backend.src.blog.service.UsersBlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/blogs")
public class UsersBlogApi {

    private final UsersBlogService usersBlogService;

    @PostMapping(value = "/validation/blog-name", consumes = APPLICATION_JSON_VALUE)
    public Map<String, Boolean> validateBlogName(@RequestBody Map<String, String> requestData) {
        String blogName = requestData.get("blogName");

        boolean isDuplicated = usersBlogService.duplicateBlogName(blogName);

        Map<String, Boolean> map = new HashMap<>();
        map.put("isDuplicated", isDuplicated);
        return map;
    }

    @PostMapping(value = "/validation/blog-url", consumes = APPLICATION_JSON_VALUE)
    public Map<String, Boolean> validateBlogUrl(@RequestBody Map<String, String> requestData) {
        String blogUrl = requestData.get("blogUrl");

        boolean isDuplicated = usersBlogService.duplicateBlogUrl(blogUrl);

        Map<String, Boolean> map = new HashMap<>();
        map.put("isDuplicated", isDuplicated);
        return map;
    }
}
