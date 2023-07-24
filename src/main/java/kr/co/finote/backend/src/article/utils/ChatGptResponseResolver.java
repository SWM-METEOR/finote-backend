package kr.co.finote.backend.src.article.utils;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.finote.backend.src.article.domain.AiSearchType;
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

    public AiSearchResponse resolve(List<ChatgptResponse.Choice> choices, AiSearchType aiSearchType) {

        String gptResponse =
                choices.stream()
                        .map(choice -> choice.getMessage().getContent())
                        .collect(Collectors.joining(""));

        String content = aiSearchType.getRESPONSE_PREFIX() + gptResponse;
        log.info("답변 = {}", content);

        return new AiSearchResponse(aiSearchType.getType(), content);
    }
}
