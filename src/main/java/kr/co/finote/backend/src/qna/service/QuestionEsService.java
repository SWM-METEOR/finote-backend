package kr.co.finote.backend.src.qna.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.ConnectException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.qna.document.QuestionDocument;
import kr.co.finote.backend.src.qna.domain.Question;
import kr.co.finote.backend.src.qna.repository.QuestionEsRepository;
import kr.co.finote.backend.src.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionEsService {

    private final QuestionEsRepository questionEsRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    private final int MAX_CALL_COUNT = 3;

    public void save(Question question, User user) {
        int callCount = 0;
        boolean isSaved = false;

        while (callCount < MAX_CALL_COUNT) {
            callCount += 1;
            try {
                questionEsRepository.save(QuestionDocument.createQuestionDocument(question, user));
                isSaved = true;
                break;
            } catch (Exception e) {
                log.info("Save Question Document : {}", callCount);
            }
        }
        if (!isSaved) throw new ConnectException(ResponseCode.ES_NOT_CONNECT);
    }

    public void editDocumentByQuestion(Question question) {
        List<QuestionDocument> questionDocuments =
                questionEsRepository.findByQuestionId(question.getId());

        if (questionDocuments.isEmpty()) {
            throw new NotFoundException(ResponseCode.QUESTION_NOT_FOUND);
        }

        QuestionDocument questionDocument = questionDocuments.get(0);
        questionDocument.editByQuestion(question);
        // Dirty checking 지원 안되므로, 명시적으로 처리
        questionEsRepository.save(questionDocument);
    }

    public void editDocumentByUser(User user) {
        List<QuestionDocument> questionDocuments =
                questionEsRepository.findByAuthorNickname(user.getNickname());

        questionDocuments.forEach(questionDocument -> questionDocument.editByUser(user));
        questionEsRepository.saveAll(questionDocuments);
    }

    public void deleteDocument(Long questionId) {
        questionEsRepository.deleteByQuestionId(questionId);
    }

    public List<QuestionDocument> inlineQna(String searchText) {
        SearchHits<QuestionDocument> results = search(searchText);

        List<SearchHit<QuestionDocument>> searchHits =
                results.getSearchHits().stream().sorted(scoreComparator()).collect(Collectors.toList());

        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    private SearchHits<QuestionDocument> search(String searchText) {
        NativeSearchQuery searchQuery = getNativeSearchQuery(searchText);
        return elasticsearchRestTemplate.search(searchQuery, QuestionDocument.class);
    }

    private NativeSearchQuery getNativeSearchQuery(String searchText) {
        QueryStringQueryBuilder builder = QueryBuilders.queryStringQuery(searchText);
        return new NativeSearchQuery(builder);
    }

    public Comparator<SearchHit<QuestionDocument>> scoreComparator() {
        return (o1, o2) -> {
            if (o1.getScore() != o2.getScore()) {
                return Float.compare(o2.getScore(), o1.getScore());
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate date1 = LocalDate.parse(o1.getContent().getCreatedDate(), formatter);
            LocalDate date2 = LocalDate.parse(o2.getContent().getCreatedDate(), formatter);
            return date2.compareTo(date1);
        };
    }
}
