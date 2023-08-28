package kr.co.finote.backend.src.article.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.Keyword;
import kr.co.finote.backend.src.article.dto.KeywordScore;
import kr.co.finote.backend.src.article.dto.request.KeywordDataRequest;
import kr.co.finote.backend.src.article.dto.response.KeywordDataResponse;
import kr.co.finote.backend.src.article.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    private final ArticleKeywordService articleKeywordService;

    @Value("${KEYWORD_EXTRACTOR_URL}")
    private String keywordExtractorUrl;

    public void extractAndSaveKeywords(Article saveArticle) throws JsonProcessingException {
        KeywordDataResponse[] keywordDataResponses = extractKeywords(saveArticle.getBody()); // 키워드 추출
        if (keywordDataResponses != null) {
            List<KeywordScore> keywordScoreList =
                    saveNewKeywords(keywordDataResponses); // 새로들어온 키워드 저장 및 키워드와 스코어 반환
            articleKeywordService.saveArticleKeywordList(
                    saveArticle, keywordScoreList); // 키워드와 아티클 연관관계 저장
        }
    }

    public List<KeywordScore> saveNewKeywords(KeywordDataResponse... keywordDataResponseList) {
        List<KeywordScore> keywordScoreList = new ArrayList<>();
        for (KeywordDataResponse keywordDataResponse : keywordDataResponseList) {
            String value = keywordDataResponse.getKeyword();
            Optional<Keyword> findKeyword = keywordRepository.findByValueAndIsDeleted(value, false);
            Keyword keyword =
                    findKeyword.orElseGet(() -> keywordRepository.save(Keyword.createKeyword(value)));
            KeywordScore keywordScore =
                    KeywordScore.createKeywordScore(keyword, keywordDataResponse.getScore());
            keywordScoreList.add(keywordScore);
        }
        return keywordScoreList;
    }

    public KeywordDataResponse[] extractKeywords(String body) throws JsonProcessingException {

        // 키워드 생성 요청 entity 생성 및 json 변환
        KeywordDataRequest keywordDataRequest = new KeywordDataRequest(body);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(keywordDataRequest);

        // 헤더 및 요청 타입 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

        // 키워드 추출 서버에 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(keywordExtractorUrl, HttpMethod.POST, requestEntity, String.class);

        // 키워드 추출 서버 응답에 따른 응답 데이터 처리
        if (Objects.equals(responseEntity.getBody(), "\"value error\"")) return null;
        else return mapper.readValue(responseEntity.getBody(), KeywordDataResponse[].class);
    }
}
