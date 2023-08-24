package kr.co.finote.backend.src.common.repository;

import java.util.List;
import java.util.Optional;
import kr.co.finote.backend.src.common.domain.FollowInfo;
import kr.co.finote.backend.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowInfoRepository extends JpaRepository<FollowInfo, Long> {
    Optional<FollowInfo> findByFromUserAndToUser(User fromUser, User toUser);

    @Query("select fi from FollowInfo fi join fetch fi.toUser WHERE fi.fromUser = :fromUser")
    List<FollowInfo> findAllWithFromUser(@Param("fromUser") User fromUser);

    @Query("select fi from FollowInfo fi join fetch fi.fromUser WHERE fi.toUser = :toUser")
    List<FollowInfo> findAllWithToUser(@Param("toUser") User toUser);
}
