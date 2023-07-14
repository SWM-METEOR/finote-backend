package kr.co.finote.backend.src.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.*;
import javax.transaction.Transactional;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleOauth;
import kr.co.finote.backend.global.authentication.oauth.google.dto.request.GoogleAccessTokenRequest;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleOauthUserInfoResponse;
import kr.co.finote.backend.global.utils.StringUtils;
import kr.co.finote.backend.src.blog.domain.UsersBlog;
import kr.co.finote.backend.src.blog.repository.UsersBlogRepository;
import kr.co.finote.backend.src.user.domain.Role;
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
    private final UsersBlogRepository usersBlogRepository;

    public GoogleAccessTokenRequest getGoogleAccessToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", googleOauth.getGoogleClientId());
        params.put("client_secret", googleOauth.getGoogleClientSecret());
        params.put("redirect_uri", googleOauth.getGoogleCallbackUrl());
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(googleOauth.getGoogleTokenUrl(), params, String.class);

        String accessToken = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleAccessTokenRequest googleAccessTokenRequest = null;
        try {
            googleAccessTokenRequest =
                    objectMapper.readValue(accessToken, GoogleAccessTokenRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return googleAccessTokenRequest;
    }

    public GoogleOauthUserInfoResponse getGoogleUserInfo(
            GoogleAccessTokenRequest googleAccessTokenRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleAccessTokenRequest.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange(
                        googleOauth.getGoogleUserInfoUrl(), HttpMethod.GET, request, String.class);

        String userInfo = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOauthUserInfoResponse googleOauthUserInfoResponse = null;
        try {
            googleOauthUserInfoResponse =
                    objectMapper.readValue(userInfo, GoogleOauthUserInfoResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return googleOauthUserInfoResponse;
    }

    @Transactional
    public Boolean saveUser(GoogleOauthUserInfoResponse userInfo) {
        Optional<User> findUser = Optional.ofNullable(userRepository.findByEmail(userInfo.getEmail()));

        if (findUser.isPresent()) {
            User user = findUser.get();
            user.setLastLoginDate(LocalDateTime.now());
            return false;
        } else {
            // 중복 nickname이 없을 때까지 랜덤 nickname 생성
            String randomNickname = StringUtils.makeRandomString();
            boolean existsByNickName = userRepository.existsByNickname(randomNickname);
            while (existsByNickName) {
                randomNickname = StringUtils.makeRandomString();
                existsByNickName = userRepository.existsByNickname(randomNickname);
            }
            User user =
                    User.builder()
                            .username(userInfo.getName())
                            .email(userInfo.getEmail())
                            .provider("Google")
                            .providerId(userInfo.getId())
                            .role(Role.USER)
                            .lastLoginDate(LocalDateTime.now())
                            .nickname(randomNickname)
                            .profileImageUrl(null)
                            .build();
            userRepository.save(user);

            UsersBlog usersBlog =
                    UsersBlog.builder()
                            .user(user)
                            .name(user.getNickname())
                            // TODO : 배포 후 + 실제 블로그 URL 전략 결정 후 수정
                            .url(user.getUsername() + "/" + user.getNickname())
                            .build();
            usersBlogRepository.save(usersBlog);
            return true;
        }
    }
}
