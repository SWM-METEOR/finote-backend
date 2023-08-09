package kr.co.finote.backend.src.user.repository;

import java.util.Optional;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdAndIsDeleted(String Id, Boolean isDeleted);

    Optional<User> findByUsernameAndIsDeleted(String username, Boolean isDeleted);

    Optional<User> findByEmailAndIsDeleted(String email, Boolean isDeleted);

    Optional<User> findByRefreshTokenAndIsDeleted(String refreshToken, Boolean isDeleted);

    boolean existsByNicknameAndIsDeleted(String nickName, Boolean isDeleted);

    boolean existsByBlogNameAndIsDeleted(String blogName, Boolean isDeleted);

    boolean existsByBlogUrlAndIsDeleted(String blogUrl, Boolean isDeleted);
}
