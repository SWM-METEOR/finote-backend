package kr.co.finote.backend.src.article.service;

import kr.co.finote.backend.src.article.dto.response.AiSearchResponse;
import kr.co.finote.backend.src.article.dto.response.ChatgptResponse;
import kr.co.finote.backend.src.article.utils.ChatGptRequestSender;
import kr.co.finote.backend.src.article.utils.ChatGptResponseResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiSearchService {

    private final ChatGptRequestSender chatGptRequestSender;
    private final ChatGptResponseResolver chatGptResponseResolver;

    public AiSearchResponse getResponse(String prompt) {

        ChatgptResponse chatgptResponse = chatGptRequestSender.sendMessage(prompt);
        return chatGptResponseResolver.resolve(chatgptResponse.getChoices());
    }
}
