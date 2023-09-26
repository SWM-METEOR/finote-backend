package kr.co.finote.backend.src.user.repository;

import kr.co.finote.backend.src.user.domain.Category;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameAndUserAndIsDeleted(String name, User user, Boolean isDeleted);
}
