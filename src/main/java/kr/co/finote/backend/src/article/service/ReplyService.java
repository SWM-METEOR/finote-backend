package kr.co.finote.backend.src.article.service;

import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.Reply;
import kr.co.finote.backend.src.article.dto.request.ReplyRequest;
import kr.co.finote.backend.src.article.dto.response.PostReplyResponse;
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
        Reply saveReply = saveReply(loginUser, request, article);
        return PostReplyResponse.createPostReplyResponse(saveReply.getId());
    }

    @NotNull
    private Reply saveReply(User loginUser, ReplyRequest request, Article article) {
        Reply reply = Reply.createReply(loginUser, article, request.getContent());
        return replyRepository.save(reply);
    }
}
