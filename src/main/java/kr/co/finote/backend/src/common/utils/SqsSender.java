package kr.co.finote.backend.src.common.utils;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import kr.co.finote.backend.src.article.dto.request.FeedRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqsSender {

    @Value("${QUEUE_URL}")
    private String url;

    private final ObjectMapper objectMapper;
    private final AmazonSQS amazonSQS;

    public SendMessageResult sendMessage(FeedRequest request) throws JsonProcessingException {
        SendMessageRequest sendMessageRequest =
                new SendMessageRequest(url, objectMapper.writeValueAsString(request))
                        .withMessageGroupId("sqs-test")
                        .withMessageDeduplicationId(UUID.randomUUID().toString());

        return amazonSQS.sendMessage(sendMessageRequest);
    }
}
