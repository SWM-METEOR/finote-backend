package kr.co.finote.backend.src.common.api;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.finote.backend.global.annotation.Login;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.src.common.dto.FollowResultResponse;
import kr.co.finote.backend.src.common.service.FollowService;
import kr.co.finote.backend.src.user.domain.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowApi {

    FollowService followService;

    @Operation(summary = "유저 팔로우", description = "팔로우 할 유저의 닉네임을 Path-variable로 전달")
    @PostMapping("/follow/{nickname}")
    public FollowResultResponse follow(
            @Login User loginUser, @PathVariable("nickname") String nickname) {
        if (loginUser.getNickname().equals(nickname)) {
            throw new InvalidInputException(ResponseCode.SELF_FOLLOWING);
        }
        return followService.follow(loginUser, nickname);
    }

    @Operation(summary = "유저 언팔로우", description = "언팔로우 할 유저의 닉네임을 Path-variable로 전달")
    @PostMapping("/unfollow/{nickname}")
    public FollowResultResponse unfollow(
            @Login User loginUser, @PathVariable("nickname") String nickname) {
        if (loginUser.getNickname().equals(nickname)) {
            throw new InvalidInputException(ResponseCode.SELF_FOLLOWING);
        }
        return followService.unfollow(loginUser, nickname);
    }
}
