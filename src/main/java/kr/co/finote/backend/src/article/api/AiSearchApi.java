package kr.co.finote.backend.src.article.api;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import kr.co.finote.backend.src.article.domain.AiSearchType;
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

    @Operation(summary = "AI 검색 - 의미")
    @PostMapping("/ai-search/meaning")
    public AiSearchResponse meaning(@RequestBody @Valid AiSearchRequest aiSearchRequest) {

        String prompt = aiSearchRequest.getPrompt() + ChatGptPromptUtils.MEANING_SUFFIX;
        return aiSearchService.getResponse(prompt, AiSearchType.MEANING);
    }

    @Operation(summary = "AI 검색 - 추가 설명")
    @PostMapping("/ai-search/explanation")
    public AiSearchResponse explanation(@RequestBody @Valid AiSearchRequest aiSearchRequest) {

        String prompt = aiSearchRequest.getPrompt() + ChatGptPromptUtils.EXPLAIN_SUFFIX;
        return aiSearchService.getResponse(prompt, AiSearchType.EXPLANATION);
    }

    @Operation(summary = "AI 검색 - 코드 설명")
    @PostMapping("/ai-search/code")
    public AiSearchResponse code(@RequestBody @Valid AiSearchRequest aiSearchRequest) {

        String prompt = aiSearchRequest.getPrompt() + ChatGptPromptUtils.CODE_SUFFIX;
        return aiSearchService.getResponse(prompt, AiSearchType.CODE);
    }
}
