package kr.co.finote.backend.src.blog.repository;

import java.util.Optional;
import kr.co.finote.backend.src.blog.domain.UsersBlog;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersBlogRepository extends JpaRepository<UsersBlog, String> {
    boolean existsByUrl(String url);

    boolean existsByName(String name);

    Optional<UsersBlog> findByUserAndIsDeleted(User user, Boolean isDeleted);
}
