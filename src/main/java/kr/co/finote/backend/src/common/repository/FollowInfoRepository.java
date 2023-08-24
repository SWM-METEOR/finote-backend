package kr.co.finote.backend.src.common.repository;

import java.util.Optional;
import kr.co.finote.backend.src.common.domain.FollowInfo;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowInfoRepository extends JpaRepository<FollowInfo, Long> {
    Optional<FollowInfo> findByFromUserAndToUser(User fromUser, User toUser);
}
