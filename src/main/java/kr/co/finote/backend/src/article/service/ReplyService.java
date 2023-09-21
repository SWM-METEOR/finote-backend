package kr.co.finote.backend.src.article.service;

import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.Reply;
import kr.co.finote.backend.src.article.dto.request.ReplyRequest;
import kr.co.finote.backend.src.article.dto.response.PostReplyResponse;
import kr.co.finote.backend.src.article.dto.response.ReplyListResponse;
import kr.co.finote.backend.src.article.dto.response.ReplyResponse;
import kr.co.finote.backend.src.article.repository.ReplyRepository;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ArticleService articleService;

    public PostReplyResponse postReply(
            User loginUser, String nickname, String title, ReplyRequest request) {
        Article article = articleService.findByNicknameAndTitle(nickname, title);
        boolean mine = isMine(article.getUser().getEmail(), loginUser.getEmail());
        Reply saveReply = saveReply(loginUser, request, article, mine);
        return PostReplyResponse.createPostReplyResponse(saveReply.getId());
    }

    @NotNull
    private Reply saveReply(User loginUser, ReplyRequest request, Article article, boolean mine) {
        Reply reply = Reply.createReply(loginUser, article, request.getContent(), mine);
        return replyRepository.save(reply);
    }

    public ReplyListResponse getReplies(String nickname, String title) {
        Article article = articleService.findByNicknameAndTitle(nickname, title);
        List<Reply> replyList = replyRepository.findByArticleAndIsDeleted(article, false);
        List<ReplyResponse> replyResponseList = getReplyResponses(replyList);
        return ReplyListResponse.of(replyResponseList);
    }

    @NotNull
    private static List<ReplyResponse> getReplyResponses(List<Reply> replyList) {
        List<ReplyResponse> replyResponseList = new ArrayList<>();
        for (Reply reply : replyList) {
            replyResponseList.add(ReplyResponse.of(reply));
        }
        return replyResponseList;
    }

    private static boolean isMine(String authorEmail, String loginUserEmail) {
        return authorEmail.equals(loginUserEmail);
    }
}
