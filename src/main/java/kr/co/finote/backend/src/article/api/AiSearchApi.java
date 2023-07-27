package kr.co.finote.backend.src.article.api;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import kr.co.finote.backend.src.article.dto.request.AiSearchRequest;
import kr.co.finote.backend.src.article.dto.response.AiSearchResponse;
import kr.co.finote.backend.src.article.service.AiSearchService;
import kr.co.finote.backend.src.article.utils.ChatGptPromptUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class AiSearchApi {

    private final AiSearchService aiSearchService;

    @Operation(summary = "AI 검색")
    @PostMapping("/ai-search")
    public AiSearchResponse meaning(@RequestBody @Valid AiSearchRequest aiSearchRequest) {

        String prompt = aiSearchRequest.getPrompt() + ChatGptPromptUtils.PROMPT_SUFFIX;
        return aiSearchService.getResponse(prompt);
    }
}
