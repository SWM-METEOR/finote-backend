package kr.co.finote.backend.src.article.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleViewCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean hasViewCache(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void cacheView(String key) {
        redisTemplate.opsForValue().set(key, true);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight =
                now.toLocalDate().toDateTimeAtStartOfDay().plusDays(1).toLocalDateTime();
        long validTime = midnight.toDateTime().getMillis() - now.toDateTime().getMillis();
        redisTemplate.expire(key, validTime, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}
