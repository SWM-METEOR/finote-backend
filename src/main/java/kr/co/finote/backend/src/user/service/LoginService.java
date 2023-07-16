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
import kr.co.finote.backend.src.user.domain.Role;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.response.SaveUserResponse;
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
    public SaveUserResponse saveUser(GoogleOauthUserInfoResponse userInfo) {
        Optional<User> findUser = userRepository.findByEmailAndIsDeleted(userInfo.getEmail(), false);

        if (findUser.isPresent()) {
            User user = findUser.get();
            user.setLastLoginDate(LocalDateTime.now());
            return new SaveUserResponse(user, false);
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
                            .blogName(randomNickname)
                            .blogUrl(userInfo.getName() + "/" + randomNickname)
                            .build();
            userRepository.save(user);

            return new SaveUserResponse(user, true);
        }
    }
}
