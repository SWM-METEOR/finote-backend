package kr.co.finote.backend.src.article.service;

import kr.co.finote.backend.src.article.dto.request.AiSearchRequest;
import kr.co.finote.backend.src.article.dto.response.AiSearchResponse;
import kr.co.finote.backend.src.article.dto.response.ChatgptResponse;
import kr.co.finote.backend.src.article.gpt.ChatGptPromptUtils;
import kr.co.finote.backend.src.article.gpt.ChatGptRequestSender;
import kr.co.finote.backend.src.article.gpt.ChatGptResponseResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiSearchService {

    private final ChatGptRequestSender chatGptRequestSender;
    private final ChatGptResponseResolver chatGptResponseResolver;

    public AiSearchResponse getResponse(AiSearchRequest request) {
        String prompt = request.getPrompt() + ChatGptPromptUtils.PROMPT_SUFFIX;
        ChatgptResponse chatgptResponse = chatGptRequestSender.sendMessage(prompt);
        return chatGptResponseResolver.resolve(chatgptResponse.getChoices());
    }
}
