package kr.co.finote.backend.src.user.dto.response;

import kr.co.finote.backend.src.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlogResponse {

    private String blogName;

    public static BlogResponse of(User loginUser) {
        return new BlogResponse(loginUser.getBlogName());
    }
}
