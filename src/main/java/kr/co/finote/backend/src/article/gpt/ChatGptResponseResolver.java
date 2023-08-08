package kr.co.finote.backend.src.article.gpt;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.finote.backend.src.article.dto.response.AiSearchResponse;
import kr.co.finote.backend.src.article.dto.response.ChatgptResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatGptResponseResolver {

    public AiSearchResponse resolve(List<ChatgptResponse.Choice> choices) {

        String gptResponse =
                choices.stream()
                        .map(choice -> choice.getMessage().getContent())
                        .collect(Collectors.joining(""));

        log.info("답변 = {}", gptResponse);

        return AiSearchResponse.createAiSearchResponse(gptResponse);
    }
}
