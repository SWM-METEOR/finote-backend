package kr.co.finote.backend.src.article.utils;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.finote.backend.src.article.dto.response.AiSearchResponse;
import kr.co.finote.backend.src.article.dto.response.ChatgptResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@Slf4j
public class ChatGptResponseResolver {

    public AiSearchResponse resolve(List<ChatgptResponse.Choice> choices) {

        String gptResponse =
                choices.stream()
                        .map(choice -> choice.getMessage().getContent())
                        .collect(Collectors.joining(""));

        log.info("답변 = {}", gptResponse);

        String[] parts = gptResponse.split("추가설명:\n");

        String meaning = parts[0];
        // TODO : 질문 내용을 조금 더 고민해서 의미 / 추가설명을 받을 수 있게해야함.
        String explanation = parts.length > 1 ? parts[1] : null;

        log.info("의미 = {}", meaning);
        log.info("추가설명 = {}", explanation);

        return new AiSearchResponse(meaning, explanation);
    }
}
