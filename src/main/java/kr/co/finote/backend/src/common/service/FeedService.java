package kr.co.finote.backend.src.common.service;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.co.finote.backend.src.article.dto.request.FeedRequest;
import kr.co.finote.backend.src.common.domain.FollowInfo;
import kr.co.finote.backend.src.common.utils.SqsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final SqsSender sqsSender;
    private final FollowService followService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public SendMessageResult publish(FeedRequest request) throws JsonProcessingException {
        return sqsSender.sendMessage(request);
    }

    @SqsListener(value = "${QUEUE_NAME}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void fanout(@Payload String info, Acknowledgment ack) throws JsonProcessingException {
        FeedRequest request = objectMapper.readValue(info, FeedRequest.class);

        List<FollowInfo> followInfos = followService.followersByAuthorId(request.getAuthorId());
        ListOperations<String, Object> listOps = redisTemplate.opsForList();

        for (FollowInfo followInfo : followInfos) {
            String key = followInfo.getFromUser().getId() + ":feed";
            listOps.leftPush(key, request.getArticleId());
        }

        ack.acknowledge();
    }
}
