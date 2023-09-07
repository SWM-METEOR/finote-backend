package kr.co.finote.backend.global.config;

import kr.co.finote.backend.src.article.service.ElasticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchTester {

    private final ElasticService elasticService;

    @Scheduled(fixedDelay = 45000, initialDelay = 10000)
    public void keepConnectionAlive() {
        log.info("ElasticSearch Connection Alive");
        try {
            elasticService.search("");
            log.info("ElasticSearchRestTemplate.search()..");
        } catch (Exception e) {
            log.error("ElasticSearch Connection Error");
        }
    }
}
