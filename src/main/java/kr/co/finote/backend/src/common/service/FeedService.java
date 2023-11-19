package kr.co.finote.backend.src.common.service;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.dto.request.FeedRequest;
import kr.co.finote.backend.src.article.repository.ArticleRepository;
import kr.co.finote.backend.src.common.domain.FollowInfo;
import kr.co.finote.backend.src.common.dto.response.FeedListResponse;
import kr.co.finote.backend.src.common.dto.response.FeedResponse;
import kr.co.finote.backend.src.common.utils.SqsSender;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    private final SqsSender sqsSender;
    private final FollowService followService;
    private final ArticleRepository articleRepository;

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String FEED_SUFFIX = ":feed";
    private static final int FEED_OFFSET_START = 0;
    private static final int FEED_OFFSET_END = 4;

    public SendMessageResult publish(FeedRequest request) throws JsonProcessingException {
        return sqsSender.sendMessage(request);
    }

    @SqsListener(value = "${QUEUE_NAME}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void fanout(@Payload String info, Acknowledgment ack) throws JsonProcessingException {
        FeedRequest request = objectMapper.readValue(info, FeedRequest.class);

        List<FollowInfo> followInfos = followService.followersByAuthorId(request.getAuthorId());
        ListOperations<String, Object> listOps = getListOps();

        for (FollowInfo followInfo : followInfos) {
            String key = followInfo.getFromUser().getId() + ":feed";
            listOps.leftPush(key, request.getArticleId());
        }

        ack.acknowledge();
    }

    public FeedListResponse feeds(User user) {
        String redisKey = user.getId() + FEED_SUFFIX;

        ListOperations<String, Object> listOps = getListOps();
        List<Object> articleIds =
                Objects.requireNonNull(listOps.range(redisKey, FEED_OFFSET_START, FEED_OFFSET_END));

        List<FeedResponse> lists = new ArrayList<>();

        for (Object id : articleIds) {
            Integer intId = (Integer) id;
            Article findArticle = findByArticleId(intId.longValue());
            FeedResponse response = FeedResponse.of(findArticle, findArticle.getUser());
            lists.add(response);
        }
        return FeedListResponse.of(lists);
    }

    private ListOperations<String, Object> getListOps() {
        return redisTemplate.opsForList();
    }

    private Article findByArticleId(Long id) {
        return articleRepository
                .findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));
    }
}
