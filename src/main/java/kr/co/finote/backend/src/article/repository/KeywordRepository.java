package kr.co.finote.backend.src.article.repository;

import java.util.Optional;
import kr.co.finote.backend.src.article.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByValueAndIsDeleted(String value, boolean isDeleted);
}
