package kr.co.finote.backend.src.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.util.*;
import kr.co.finote.backend.global.authentication.oauth.google.GoogleOauth;
import kr.co.finote.backend.global.authentication.oauth.google.dto.request.GoogleAccessToken;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleLoginResponse;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleUserInfo;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.UnAuthorizedException;
import kr.co.finote.backend.global.jwt.JwtToken;
import kr.co.finote.backend.global.utils.StringUtils;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.EmailLoginRequest;
import kr.co.finote.backend.src.user.dto.response.EmailLoginResponse;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final GoogleOauth googleOauth;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

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

        return GoogleAccessToken.createGoogleAccessToken(accessToken);
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

        return GoogleUserInfo.createGoogleUserInfo(userInfo);
    }

    @Transactional
    public GoogleLoginResponse addOrUpdateUser(GoogleUserInfo response) {
        Optional<User> findUser = userRepository.findByEmailAndIsDeleted(response.getEmail(), false);

        if (findUser.isPresent()) {
            User user = findUser.get();
            user.updateLastLoginDate(LocalDateTime.now());
            String accessTokenByEmail = jwtService.createAccessTokenByEmail(user.getEmail());
            String refreshToken = jwtService.createRefreshToken();
            return GoogleLoginResponse.oldUser(user, accessTokenByEmail, refreshToken);
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
            String accessTokenByEmail = jwtService.createAccessTokenByEmail(user.getEmail());
            String refreshToken = jwtService.createRefreshToken();
            return GoogleLoginResponse.freshUser(user, accessTokenByEmail, refreshToken);
        }
    }

    @Transactional
    public EmailLoginResponse loginByEmail(EmailLoginRequest emailLoginRequest) {
        String email = emailLoginRequest.getEmail();
        String password = emailLoginRequest.getPassword();
        Optional<User> findByEmail = userRepository.findByEmailAndIsDeleted(email, false);
        if (findByEmail.isPresent()) {
            // 해당 이메일로 가입된 로그인 유저 확인
            User loginUser = findByEmail.get();
            // 비밀번호 검증을 위한 salting
            String inputPasswordSalted = password + "_" + email.split("@")[0];
            // 암호화된 비밀번호 검증
            boolean matches = bCryptPasswordEncoder.matches(inputPasswordSalted, loginUser.getPassword());
            if (matches) {
                loginUser.updateLastLoginDate(LocalDateTime.now());
                // 새로운 access token 생성
                String accessTokenByEmail = jwtService.createAccessTokenByEmail(loginUser.getEmail());
                // 새로운 refresh token 생성
                String refreshToken = jwtService.createRefreshToken();
                // 로그인 성공 및 새로운 access token, refresh token 생성
                return EmailLoginResponse.createSuccessEmailLoginResponse(
                        loginUser, accessTokenByEmail, refreshToken);
            } else {
                // 비밀번호가 일치하지 않을 경우 실패 응답 반환
                return EmailLoginResponse.createFailEmailLoginResponse();
            }
        } else {
            // 해당 이메일로 가입된 유저가 존재하지 않을 경우 실패 응답 반환
            return EmailLoginResponse.createFailEmailLoginResponse();
        }
    }

    @Transactional
    public void logout(JwtToken jwtToken) {
        String refreshToken = jwtToken.getRefreshToken();

        User user =
                userRepository
                        .findByRefreshTokenAndIsDeleted(refreshToken, false)
                        .orElseThrow(() -> new UnAuthorizedException(ResponseCode.INVALID_REFRESH_TOKEN));

        user.updateRefreshToken(null);
    }
}
