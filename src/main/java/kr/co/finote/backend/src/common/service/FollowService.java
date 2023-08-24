package kr.co.finote.backend.src.common.service;

import java.util.Optional;
import javax.transaction.Transactional;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.src.common.domain.FollowInfo;
import kr.co.finote.backend.src.common.dto.FollowResultResponse;
import kr.co.finote.backend.src.common.repository.FollowInfoRepository;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowService {

    FollowInfoRepository followInfoRepository;
    UserService userService;

    @Transactional
    public FollowResultResponse follow(User fromUser, String toUserNickname) {
        User toUser = userService.findByNickname(toUserNickname);

        Optional<FollowInfo> result = followInfoRepository.findByFromUserAndToUser(fromUser, toUser);

        if (result.isPresent()) {
            throw new InvalidInputException(ResponseCode.ALREADY_FOLLOWING);
        }

        followInfoRepository.save(FollowInfo.of(fromUser, toUser));
        return FollowResultResponse.of(true);
    }

    @Transactional
    public FollowResultResponse unfollow(User fromUser, String toUserNickname) {
        User toUser = userService.findByNickname(toUserNickname);

        Optional<FollowInfo> result = followInfoRepository.findByFromUserAndToUser(fromUser, toUser);

        if (result.isPresent()) {
            FollowInfo followInfo = result.get();
            followInfo.updateIsDeleted(true);
            return FollowResultResponse.of(true);
        }

        throw new InvalidInputException(ResponseCode.NOT_FOLLOWING);
    }
}
