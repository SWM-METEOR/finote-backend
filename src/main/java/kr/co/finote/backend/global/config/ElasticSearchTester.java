package kr.co.finote.backend.global.config;

import kr.co.finote.backend.src.article.repository.ArticleEsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchTester {

    private final ArticleEsRepository articleEsRepository;

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void keepConnectionAlive() {
        log.info("ElasticSearch Connection Alive");
        try {
            long count = articleEsRepository.count();
            log.info("ElasticSearch Connection Count : {}", count);
        } catch (Exception e) {
            log.error("ElasticSearch Connection Error");
        }
    }
}
