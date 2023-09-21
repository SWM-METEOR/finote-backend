package kr.co.finote.backend.src.article.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplyListResponse {

    private List<ReplyResponse> replyList;

    public static ReplyListResponse of(List<ReplyResponse> replyList) {
        return new ReplyListResponse(replyList);
    }
}
