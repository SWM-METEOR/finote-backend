package kr.co.finote.backend.src.user.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailCodeCacheService {

    private final CacheManager cacheManager;

    public String findEmailCode(String email) {
        return Objects.requireNonNull(cacheManager.getCache("EmailCode")).get(email, String.class);
    }

    @Cacheable(key = "#email", value = "EmailCode", cacheManager = "emailCodeManager")
    @Transactional
    public String cacheEmailCode(String email, String code) {
        return code;
    }

    @CacheEvict(key = "#email", value = "EmailCode", cacheManager = "emailCodeManager")
    public void evictEmailCode(String email) {}
}
