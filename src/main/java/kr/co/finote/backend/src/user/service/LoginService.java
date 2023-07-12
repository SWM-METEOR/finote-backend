package kr.co.finote.backend.src.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleAccessTokenDto;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleOauth;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleOauthUserInfoDto;
import kr.co.finote.backend.src.blog.domain.Blog;
import kr.co.finote.backend.src.blog.repository.BlogRepository;
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
    private final BlogRepository blogRepository;

    public GoogleAccessTokenDto getGoogleAccessToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", googleOauth.getGoogleClientId());
        params.put("client_secret", googleOauth.getGoogleClientSecret());
        params.put("redirect_uri", googleOauth.getGoogleCallbackUrl());
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(googleOauth.getGoogleTokenUrl(), params, String.class);

        String accessToken = responseEntity.getBody();
        log.info(accessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleAccessTokenDto googleAccessTokenDto = null;
        try {
            googleAccessTokenDto = objectMapper.readValue(accessToken, GoogleAccessTokenDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return googleAccessTokenDto;
    }

    public GoogleOauthUserInfoDto getGoogleUserInfo(GoogleAccessTokenDto googleAccessTokenDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleAccessTokenDto.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange(
                        googleOauth.getGoogleUserInfoUrl(), HttpMethod.GET, request, String.class);

        String userInfo = response.getBody();
        log.info(userInfo);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOauthUserInfoDto googleOauthUserInfoDto = null;
        try {
            googleOauthUserInfoDto = objectMapper.readValue(userInfo, GoogleOauthUserInfoDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return googleOauthUserInfoDto;
    }

    public Boolean saveUser(GoogleOauthUserInfoDto userInfo) {
        Optional<User> findUser = Optional.ofNullable(userRepository.findByEmail(userInfo.getEmail()));

        if (findUser.isPresent()) {
            log.info("present");
            User user = findUser.get();
            user.setLastLoginDate(LocalDateTime.now());
            userRepository.save(user);
            return false;
        } else {
            log.info("not present");
            User user =
                    User.builder()
                            .username(userInfo.getName())
                            .email(userInfo.getEmail())
                            .provider("Google")
                            .providerId(userInfo.getId())
                            .role(Role.USER)
                            .lastLoginDate(LocalDateTime.now())
                            .nickName(UUID.randomUUID().toString())
                            .profileImageUrl(null)
                            .build();
            userRepository.save(user);

            Blog blog =
                    Blog.builder()
                            .user(user)
                            .name(user.getNickName())
                            // TODO : 배포 후 + 실제 블로그 URL 전략 결정 후 수정
                            .url(user.getUsername() + "/" + user.getNickName())
                            .build();
            blogRepository.save(blog);
            return true;
        }
    }
}
