package kr.co.finote.backend.src.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostReplyResponse {

    private Long replyId;

    public static PostReplyResponse createPostReplyResponse(Long replyId) {
        return new PostReplyResponse(replyId);
    }
}
