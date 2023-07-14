package kr.co.finote.backend.src.user.service;

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
        return userRepository.existsByNickName(nickname);
    }
}
