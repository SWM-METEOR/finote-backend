package kr.co.finote.backend.src.article.service;

import java.util.ArrayList;
import java.util.List;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.NotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ArticleService articleService;
    private final ArticleEsService articleEsService;

    public PostReplyResponse postReply(
            User loginUser, String nickname, String title, ReplyRequest request) {
        Article article = articleService.findByNicknameAndTitle(nickname, title);
        articleService.editTotalReply(article, 1); // 댓글 등록 시 댓글 수 증가
        articleEsService.editTotalReply(
                article.getId(), article.getTotalReply()); // 댓글 수 증가 Elasticsearch에도 반영
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
        List<Reply> replyList = replyRepository.findAllWithArticle(article);
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

    @Transactional
    public PostReplyResponse editReply(User longUser, Long replyId, ReplyRequest request) {
        Reply reply = getReply(replyId);
        checkReplyAuthority(longUser, reply);
        reply.edit(request.getContent());
        return PostReplyResponse.createPostReplyResponse(reply.getId());
    }

    private static void checkReplyAuthority(User longUser, Reply reply) {
        if (!reply.getUser().getEmail().equals(longUser.getEmail())) {
            throw new NotFoundException(ResponseCode.REPLY_NOT_WRITER);
        }
    }

    private Reply getReply(Long replyId) {
        return replyRepository
                .findById(replyId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.REPLY_NOT_FOUND));
    }

    @Transactional
    public void deleteReply(User loginUser, Long replyId) {
        Reply reply = getReply(replyId);
        checkReplyAuthority(loginUser, reply);
        reply.delete();
        articleService.editTotalReply(reply.getArticle(), -1); // 댓글 삭제 시 댓글 수 감소
        articleEsService.editTotalReply(
                reply.getArticle().getId(),
                reply.getArticle().getTotalReply()); // 댓글 수 감소 Elasticsearch에도 반영
    }
}
