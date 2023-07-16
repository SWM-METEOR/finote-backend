package kr.co.finote.backend.src.user.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.CustomException;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean duplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public void editNickname(User user, String nickname) {
        if (duplicateNickname(nickname)) {
            throw new CustomException(ResponseCode.DUPLICATE_NICKNAME);
        }

        User findUser =
                userRepository
                        .findById(user.getId())
                        .orElseThrow(() -> new CustomException(ResponseCode.UNAUTHENTICATED));
        findUser.setNickname(nickname);
    }
}
