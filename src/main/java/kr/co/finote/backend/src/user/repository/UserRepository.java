package kr.co.finote.backend.src.user.repository;

import java.util.Optional;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsernameAndIsDeleted(String username, Boolean isDeleted);

    Optional<User> findByEmailAndIsDeleted(String email, Boolean isDeleted);

    boolean existsByNickname(String nickName);

    boolean existsByBlogName(String blogName);

    boolean existsByBlogUrl(String blogUrl);
}
