package kr.co.finote.backend.src.common.service;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.finote.backend.src.article.dto.request.FeedRequest;
import kr.co.finote.backend.src.common.utils.SqsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final SqsSender sqsSender;

    public SendMessageResult publish(FeedRequest request) throws JsonProcessingException {
        return sqsSender.sendMessage(request);
    }
}
