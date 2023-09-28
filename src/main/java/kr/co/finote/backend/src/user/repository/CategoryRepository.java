package kr.co.finote.backend.src.user.repository;

import java.util.Optional;
import kr.co.finote.backend.src.user.domain.Category;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    boolean existsByNameAndUserAndIsDeleted(String name, User user, Boolean isDeleted);
}
