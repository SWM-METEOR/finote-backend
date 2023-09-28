package kr.co.finote.backend.src.user.repository;

import java.util.List;
import java.util.Optional;
import kr.co.finote.backend.src.user.domain.Category;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    boolean existsByNameAndUserAndIsDeleted(String name, User user, Boolean isDeleted);

    List<Category> findAllByUserAndIsDeletedOrderByCreatedDate(User user, Boolean isDeleted);

    Optional<Category> findByNameAndIsDeleted(String name, Boolean isDeleted);
}
