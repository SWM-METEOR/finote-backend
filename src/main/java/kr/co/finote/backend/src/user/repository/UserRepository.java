package kr.co.finote.backend.src.user.repository;

import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    User findByEmail(String email);

    boolean existsByNickName(String nickName);
}
