package kr.co.finote.backend.src.common.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.src.common.domain.FollowInfo;
import kr.co.finote.backend.src.common.dto.response.FollowResultResponse;
import kr.co.finote.backend.src.common.dto.response.FollowUserListResponse;
import kr.co.finote.backend.src.common.dto.response.FollowUserResponse;
import kr.co.finote.backend.src.common.repository.FollowInfoRepository;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowService {

    FollowInfoRepository followInfoRepository;
    UserService userService;

    @Transactional
    public FollowResultResponse follow(User fromUser, String toUserNickname) {
        if (fromUser.getNickname().equals(toUserNickname)) {
            throw new InvalidInputException(ResponseCode.SELF_FOLLOWING);
        }

        User toUser = userService.findByNickname(toUserNickname);

        Optional<FollowInfo> result = followInfoRepository.findByFromUserAndToUser(fromUser, toUser);

        if (result.isPresent()) {
            FollowInfo followInfo = result.get();
            if (!followInfo.getIsDeleted())
                throw new InvalidInputException(ResponseCode.ALREADY_FOLLOWING);

            followInfo.updateIsDeleted(false);
            return FollowResultResponse.of(true);
        }

        followInfoRepository.save(FollowInfo.of(fromUser, toUser));
        return FollowResultResponse.of(true);
    }

    @Transactional
    public FollowResultResponse unfollow(User fromUser, String toUserNickname) {
        if (fromUser.getNickname().equals(toUserNickname)) {
            throw new InvalidInputException(ResponseCode.SELF_FOLLOWING);
        }

        User toUser = userService.findByNickname(toUserNickname);

        Optional<FollowInfo> result = followInfoRepository.findByFromUserAndToUser(fromUser, toUser);

        if (result.isPresent()) {
            FollowInfo followInfo = result.get();
            if (followInfo.getIsDeleted()) throw new InvalidInputException(ResponseCode.NOT_FOLLOWING);

            followInfo.updateIsDeleted(true);
            return FollowResultResponse.of(true);
        }

        throw new InvalidInputException(ResponseCode.NOT_FOLLOWING);
    }

    public FollowUserListResponse followings(String nickname) {
        User fromUser = userService.findByNickname(nickname);

        List<FollowInfo> followings = followInfoRepository.findAllWithFromUser(fromUser);
        List<FollowUserResponse> followUserResponses = FollwingUserList(followings);

        return FollowUserListResponse.of(followUserResponses);
    }

    public FollowUserListResponse followers(String nickname) {
        User toUser = userService.findByNickname(nickname);

        List<FollowInfo> followers = followInfoRepository.findAllWithToUser(toUser);
        List<FollowUserResponse> followUserResponses = FollowerUserList(followers);

        return FollowUserListResponse.of(followUserResponses);
    }

    private List<FollowUserResponse> FollowerUserList(List<FollowInfo> followInfos) {
        return followInfos.stream()
                .map(followInfo -> FollowUserResponse.of(followInfo.getFromUser()))
                .collect(Collectors.toList());
    }

    private List<FollowUserResponse> FollwingUserList(List<FollowInfo> followInfos) {
        return followInfos.stream()
                .map(followInfo -> FollowUserResponse.of(followInfo.getToUser()))
                .collect(Collectors.toList());
    }
}
