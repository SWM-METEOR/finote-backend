package kr.co.finote.backend.src.article.service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.article.document.ArticleDocument;
import kr.co.finote.backend.src.article.domain.Article;
import kr.co.finote.backend.src.article.domain.ArticleKeyword;
import kr.co.finote.backend.src.article.domain.Keyword;
import kr.co.finote.backend.src.article.dto.request.ArticleRequest;

import kr.co.finote.backend.src.article.dto.request.dragArticleRequest;
import kr.co.finote.backend.src.article.repository.ArticleEsRepository;

import kr.co.finote.backend.src.article.dto.request.KeywordDataRequest;
import kr.co.finote.backend.src.article.dto.response.KeywordDataResponse;
import kr.co.finote.backend.src.article.repository.ArticleKeywordRepository;

import kr.co.finote.backend.src.article.repository.ArticleRepository;
import kr.co.finote.backend.src.article.repository.KeywordRepository;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.response.UserArticlesResponse;
import lombok.RequiredArgsConstructor;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {


    private final ArticleRepository articleRepository;
    private final ElasticsearchRestTemplate restTemplate;
    private final ArticleEsRepository articleEsRepository;
    private final KeywordRepository keywordRepository;
    private final ArticleKeywordRepository articleKeywordRepository;

    @Value("${KEYWORD_EXTRACTOR_URL}")
    private String keywordExtractorUrl;

    public void saveDocument(Long articleId, ArticleRequest articleRequest, User loginUser) {
        ArticleDocument document = ArticleDocument.createDocument(articleId, articleRequest, loginUser);
        articleEsRepository.save(document);
    }

    public Article findById(Long articleId) {
        return articleRepository
                .findByIdAndIsDeleted(articleId, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.ARTICLE_NOT_FOUND));
    }

    public UserArticlesResponse getDragRelatedArticle(
            int page, int size, dragArticleRequest request) {
        SearchHits<ArticleDocument> byTitle = search(page, size, request.getDragText());

        List<SearchHit<ArticleDocument>> searchHits =
                byTitle.getSearchHits().stream()
                        .sorted(
                                (o1, o2) -> {
                                    if (o1.getScore() != o2.getScore()) {
                                        return Float.compare(o2.getScore(), o1.getScore());
                                    }
                                    LocalDate date1 = LocalDate.parse(o1.getContent().getCreatedDate());
                                    LocalDate date2 = LocalDate.parse(o2.getContent().getCreatedDate());

                                    return date2.compareTo(date1);
                                })
                        .collect(Collectors.toList());

        List<ArticleDocument> documents =
                searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

        return UserArticlesResponse.fromDocuments(page, size, documents);
    }

    public SearchHits<ArticleDocument> search(int page, int size, String dragText) {
        int newPage = page - 1;
        Pageable pageable = PageRequest.of(newPage, size);

        QueryStringQueryBuilder builder = QueryBuilders.queryStringQuery(dragText);

        NativeSearchQuery searchQuery = new NativeSearchQuery(builder);
        searchQuery.setPageable(pageable);

        return restTemplate.search(searchQuery, ArticleDocument.class);

    @Transactional
    public Long save(ArticleRequest articleRequest, User loginUser) throws JsonProcessingException {
        Article article = Article.createArticle(articleRequest, loginUser);
        Article saveArticle = articleRepository.save(article); // 새로운 아티클 저장

        //         test 용도라 우선 지우기 않음
        //                Article saveArticle =
        //                        articleRepository
        //                                .findByIdAndIsDeleted(1L, false)
        //                                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다.
        // id=" +
        //         1L));

        KeywordDataResponse[] keywordDataResponses = extractKeywords(saveArticle.getBody()); // 키워드 추출
        if (keywordDataResponses != null) {
            List<Keyword> keywordList = saveNewKeywords(keywordDataResponses); // 새로들어온 키워드 저장
            saveArticleKeywordList(saveArticle, keywordList); // 키워드와 아티클 연관관계 저장
        }
        return saveArticle.getId();
    }

    public List<Keyword> saveNewKeywords(KeywordDataResponse... keywordDataResponseList) {
        List<Keyword> keywordList = new ArrayList<>();
        for (KeywordDataResponse keywordDataResponse : keywordDataResponseList) {
            String value = keywordDataResponse.getKeyword();
            Optional<Keyword> findKeyword = keywordRepository.findByValueAndIsDeleted(value, false);
            if (findKeyword.isEmpty()) {
                Keyword newKeyword =
                        keywordRepository.save(
                                Keyword.createKeyword(value)); // 새로운 키워드 저장(중복체크는 saveUniqueKeyword 메서드에서 처리
                keywordList.add(newKeyword);
            } else {
                keywordList.add(findKeyword.get());
            }
        }
        return keywordList;
    }

    public void saveArticleKeywordList(Article article, List<Keyword> keywordList) {
        for (Keyword keyword : keywordList) {
            ArticleKeyword articleKeyword = ArticleKeyword.createArticleKeyword(article, keyword);
            articleKeywordRepository.save(articleKeyword);
        }
    }

    public KeywordDataResponse[] extractKeywords(String body) throws JsonProcessingException {

        // 키워드 생성 요청 entity 생성 및 json 변환
        KeywordDataRequest keywordDataRequest = new KeywordDataRequest(body);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(keywordDataRequest);

        log.info(jsonString);

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
