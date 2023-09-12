package kr.co.finote.backend.src.user.service;

import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.global.utils.StringUtils;
import kr.co.finote.backend.src.user.domain.User;
import kr.co.finote.backend.src.user.dto.request.AdditionalInfoRequest;
import kr.co.finote.backend.src.user.dto.request.EmailCodeRequest;
import kr.co.finote.backend.src.user.dto.request.EmailCodeValidationRequest;
import kr.co.finote.backend.src.user.dto.request.EmailJoinRequest;
import kr.co.finote.backend.src.user.dto.response.EmailCodeValidationResponse;
import kr.co.finote.backend.src.user.dto.response.ValidationBlogNameResponse;
import kr.co.finote.backend.src.user.dto.response.ValidationEmailResponse;
import kr.co.finote.backend.src.user.dto.response.ValidationNicknameResponse;
import kr.co.finote.backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder bcryptPasswordEncoder;
    private final MailService mailService;
    private final EmailJoinCacheService emailJoinCacheService;

    public ValidationNicknameResponse validateNickname(String nickname) {
        boolean existsByNickname = userRepository.existsByNicknameAndIsDeleted(nickname, false);
        return ValidationNicknameResponse.createValidationNicknameResponse(existsByNickname);
    }

    public ValidationBlogNameResponse validateBlogName(String blogName) {
        boolean existsByBlogName = userRepository.existsByBlogNameAndIsDeleted(blogName, false);
        return ValidationBlogNameResponse.createValidationBlogNameResponse(existsByBlogName);
    }

    @Transactional
    public void editAdditionalInfo(User user, AdditionalInfoRequest request) {
        validateNickname(request.getNickname());
        validateBlogName(request.getBlogName());

        User findUser =
                userRepository
                        .findByIdAndIsDeleted(user.getId(), false)
                        .orElseThrow(() -> new NotFoundException(ResponseCode.USER_NOT_FOUND));

        findUser.updateAdditionalInfo(request);
    }

    public User findById(String userId) {
        return userRepository
                .findByIdAndIsDeleted(userId, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.USER_NOT_FOUND));
    }

    public User findByNickname(String nickname) {
        return userRepository
                .findByNicknameAndIsDeleted(nickname, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.USER_NOT_FOUND));
    }

    public User findByEmail(String email) {
        return userRepository
                .findByEmailAndIsDeleted(email, false)
                .orElseThrow(() -> new NotFoundException(ResponseCode.USER_NOT_FOUND));
    }

    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    public void issueEmailCode(EmailCodeRequest request) {
        String emailCode =
                emailJoinCacheService.findEmailCode(request.getEmail()); // 캐시에 저장된 이메일 코드가 있는지 확인
        if (emailCode != null) {
            emailJoinCacheService.evictEmailCode(request.getEmail()); // 기존에 발급받은 코드가 존재하면 무효화
        }
        String verifiedEmail =
                emailJoinCacheService.findVerifiedEmail(request.getEmail()); // 이미 인증된 이메일인지 확인
        if (verifiedEmail != null) {
            emailJoinCacheService.evictVerifiedEmail(request.getEmail()); // 기존에 인증된 이메일이 존재하면 무효화
        }
        String random6Number = StringUtils.makeRandom6Number();
        log.info("random6Number: {}", random6Number);
        String body = "";
        body += "<h2>" + "FINOTE" + "</h2>";
        body += "<p>" + "안녕하세요. FINOTE입니다." + "</p>";
        body += "<p>" + "저희 홈페이지를 방문해주셔서 감사합니다." + "</p>";
        body += "<p>" + "요청하신 인증 번호입니다." + "</p><br>";
        body += "<h2>" + random6Number + "</h2><br>";
        body += "<p>" + "회원가입 페이지로 돌아가서 인증번호를 입력해주세요." + "</p>";
        body += "<p>" + "감사합니다." + "</p>";
        mailService.sendMail("[FINOTE] 이메일 인증 코드입니다.", request.getEmail(), body);
        emailJoinCacheService.cacheEmailCode(request.getEmail(), random6Number); // 새로운 이메일, 인증코드 데이터 캐시
    }

    public EmailCodeValidationResponse validateEmailCode(EmailCodeValidationRequest request) {
        String emailCode =
                emailJoinCacheService.findEmailCode(request.getEmail()); // 캐시에 저장된 이메일 코드가 있는지 확인
        if (emailCode == null || !emailCode.equals(request.getCode()))
            return EmailCodeValidationResponse.createEmailValidationResponse(
                    false); // 캐시에 저장된 이메일 코드가 없으면 false 리턴
        else {
            emailJoinCacheService.evictEmailCode(
                    request.getEmail()); // 캐시에 저장된 이메일 코드가 있고, 입력한 코드와 일치하면 캐시에서 삭제
            emailJoinCacheService.cacheVerifiedEmail(request.getEmail(), emailCode); // 인증된 이메일과 코드 캐시에 저장
            return EmailCodeValidationResponse.createEmailValidationResponse(true); // true 리턴
        }
    }

    @Transactional
    public void joinByEmail(EmailJoinRequest request) {
        // 기존에 가입된 이메일 있는지 확인
        userRepository
                .findByEmailAndIsDeleted(request.getEmail(), false)
                .ifPresent(
                        user -> {
                            throw new InvalidInputException(ResponseCode.EMAIL_ALREADY_EXIST);
                        });

        // 인증 완료된 이메일인지 검증
        String verifiedEmailCode = emailJoinCacheService.findVerifiedEmail(request.getEmail());
        if (verifiedEmailCode == null) {
            throw new InvalidInputException(ResponseCode.EMAIL_NOT_VERIFIED); // 인증이 안된 이메일일 경우 에러
        } else if (!verifiedEmailCode.equals(request.getCode())) {
            throw new InvalidInputException(
                    ResponseCode.EMAIL_CODE_NOT_MATCH); // 인증은 됐는데 다른 인증 코드로 요청이 온 경우
        }

        String email = request.getEmail();
        String password = request.getPassword();
        String saltedPassword = password + "_" + email.split("@")[0]; // 이메일 앞 부분을 붙여서 비밀번호 salting
        String encodedPassword = bcryptPasswordEncoder.encode(saltedPassword);

        // 중복 닉네임이 없을 때까지 닉네임 생성
        String randomNickname = StringUtils.makeRandomString();
        boolean existsByNickName = userRepository.existsByNicknameAndIsDeleted(randomNickname, false);
        while (existsByNickName) {
            randomNickname = StringUtils.makeRandomString();
            existsByNickName = userRepository.existsByNicknameAndIsDeleted(randomNickname, false);
        }

        User user = User.newEmailUser(email, encodedPassword, randomNickname);
        userRepository.save(user);
        emailJoinCacheService.evictVerifiedEmail(request.getEmail()); // 캐시에 저장된 인증된 이메일 삭제
    }

    public ValidationEmailResponse validateEmail(String email) {
        boolean existsByEmail = userRepository.existsByEmailAndIsDeleted(email, false);
        return ValidationEmailResponse.createValidationEmailResponse(existsByEmail);
    }
}
