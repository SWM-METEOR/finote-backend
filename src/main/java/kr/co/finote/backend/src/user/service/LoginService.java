package kr.co.finote.backend.src.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.util.*;
import javax.transaction.Transactional;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleOauth;
import kr.co.finote.backend.global.authentication.oauth.google.dto.request.GoogleAccessToken;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleLoginResponse;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleUserInfo;
import kr.co.finote.backend.global.jwt.JwtTokenProvider;
import kr.co.finote.backend.global.utils.StringUtils;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final GoogleOauth googleOauth;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public GoogleAccessToken getGoogleAccessToken(String code) throws JsonProcessingException {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", googleOauth.getGoogleClientId());
        params.put("client_secret", googleOauth.getGoogleClientSecret());
        params.put("redirect_uri", googleOauth.getGoogleCallbackUrl());
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(googleOauth.getGoogleTokenUrl(), params, String.class);

        String accessToken = responseEntity.getBody();

        return GoogleAccessToken.createGoogleAccessTokenRequest(accessToken);
    }

    public GoogleUserInfo getGoogleUserInfo(GoogleAccessToken request)
            throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + request.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange(
                        googleOauth.getGoogleUserInfoUrl(), HttpMethod.GET, requestEntity, String.class);

        String userInfo = response.getBody();

        return GoogleUserInfo.createGoogleOauthUserInfoResponse(userInfo);
    }

    @Transactional
    public GoogleLoginResponse addOrUpdateUser(GoogleUserInfo response) {
        Optional<User> findUser = userRepository.findByEmailAndIsDeleted(response.getEmail(), false);

        if (findUser.isPresent()) {
            User user = findUser.get();
            user.updateLastLoginDate(LocalDateTime.now());
            return GoogleLoginResponse.oldUser(user, jwtTokenProvider);
        } else {
            // 중복 nickname이 없을 때까지 랜덤 nickname 생성
            String randomNickname = StringUtils.makeRandomString();
            boolean existsByNickName = userRepository.existsByNicknameAndIsDeleted(randomNickname, false);
            while (existsByNickName) {
                randomNickname = StringUtils.makeRandomString();
                existsByNickName = userRepository.existsByNicknameAndIsDeleted(randomNickname, false);
            }

            User user = User.newGoogleUser(response, randomNickname, LocalDateTime.now());
            userRepository.save(user);

            return GoogleLoginResponse.freshUser(user, jwtTokenProvider);
        }
    }
}
