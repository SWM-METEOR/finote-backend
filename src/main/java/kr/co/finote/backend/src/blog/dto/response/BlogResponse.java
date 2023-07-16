package kr.co.finote.backend.src.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BlogResponse {

    private String blogName;
    private String blogUrl;
}
