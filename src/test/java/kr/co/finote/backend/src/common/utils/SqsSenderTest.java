package kr.co.finote.backend.src.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.finote.backend.src.article.dto.request.FeedRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SqsSenderTest {

    @Autowired
    private SqsSender sqsSender;

    @Test
    void sendMessage() throws JsonProcessingException {
        FeedRequest request = FeedRequest.createFeedRequest("abcd", 3L);
        sqsSender.sendMessage(request);
    }
}