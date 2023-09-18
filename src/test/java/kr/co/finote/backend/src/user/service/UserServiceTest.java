package kr.co.finote.backend.src.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import kr.co.finote.backend.global.code.ResponseCode;
import kr.co.finote.backend.global.exception.InvalidInputException;
import kr.co.finote.backend.global.exception.NotFoundException;
import kr.co.finote.backend.src.user.domain.Role;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.JUnitTestContainsTooManyAsserts"})
class UserServiceTest {

    @InjectMocks private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder bcryptPasswordEncoder;
    @Mock private MailService mailService;
    @Mock private EmailJoinCacheService emailJoinCacheService;

    @Test
    @DisplayName("validateNickname - 유일한 닉네임")
    void validateNicknameValid() {
        // given
        String nickname = "nickname";
        when(userRepository.existsByNicknameAndIsDeleted(nickname, false)).thenReturn(false);

        // when
        ValidationNicknameResponse validationNicknameResponse = userService.validateNickname(nickname);

        // then
        assertThat(validationNicknameResponse.isDuplicated()).isFalse();
    }

    @Test
    @DisplayName("validateNickname - 중복 닉네임")
    void validateNicknameInvalid() {
        // given
        String nickname = "nickname";
        when(userRepository.existsByNicknameAndIsDeleted(nickname, false)).thenReturn(true);

        // when
        ValidationNicknameResponse validationNicknameResponse = userService.validateNickname(nickname);

        // then
        assertThat(validationNicknameResponse.isDuplicated()).isTrue();
    }

    @Test
    @DisplayName("validateBlogName - 유일한 블로그 이름")
    void validateBlogNameValid() {
        // given
        String blogName = "blogName";

        // when
        ValidationBlogNameResponse validationBlogNameResponse = userService.validateBlogName(blogName);

        // then
        assertThat(validationBlogNameResponse.isDuplicated()).isFalse();
    }

    @Test
    @DisplayName("validateBlogName - 중복 블로그 이름")
    void validateBlogNameInvalid() {
        // given
        String blogName = "blogName";
        when(userRepository.existsByBlogNameAndIsDeleted(blogName, false)).thenReturn(true);

        // when
        ValidationBlogNameResponse validationBlogNameResponse = userService.validateBlogName(blogName);

        // then
        assertThat(validationBlogNameResponse.isDuplicated()).isTrue();
    }

    @Test
    @DisplayName("editAdditionalInfo Success")
    void editAdditionalInfo() {
        // given
        User user =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        "profileImage",
                        "blogName",
                        "refreshToken");
        AdditionalInfoRequest request =
                new AdditionalInfoRequest("new profileImage", "new nickname", "new blogName");
        when(userRepository.findByIdAndIsDeleted(user.getId(), false)).thenReturn(Optional.of(user));

        // when
        userService.editAdditionalInfo(user, request);

        // then
        assertThat(user.getProfileImageUrl()).isEqualTo("new profileImage");
        assertThat(user.getNickname()).isEqualTo("new nickname");
        assertThat(user.getBlogName()).isEqualTo("new blogName");
    }

    @Test
    @DisplayName("editAdditionalInfo Fail - 존재하지 않는 유저")
    void editAdditionalInfoFailUserNotFound() {
        // given
        User user =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        "profileImage",
                        "blogName",
                        "refreshToken");
        AdditionalInfoRequest request =
                new AdditionalInfoRequest("new profileImage", "new nickname", "new blogName");
        when(userRepository.findByIdAndIsDeleted(user.getId(), false)).thenReturn(Optional.empty());

        // when
        NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> userService.editAdditionalInfo(user, request));

        // then
        assertThat(notFoundException.getResponseCode()).isEqualTo(ResponseCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("findById Success")
    void findById() {
        // given
        User user =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        "profileImage",
                        "blogName",
                        "refreshToken");
        String userId = "1";
        when(userRepository.findByIdAndIsDeleted(userId, false)).thenReturn(Optional.of(user));

        // when
        User findUser = userService.findById(userId);

        // then
        assertThat(findUser).isEqualTo(user);
    }

    @Test
    @DisplayName("findById Fail - 존재하지 않는 유저")
    void findByIdFailUserNotFound() {
        // given
        String userId = "1";
        when(userRepository.findByIdAndIsDeleted(userId, false)).thenReturn(Optional.empty());

        // when
        NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> userService.findById(userId));

        // then
        assertThat(notFoundException.getResponseCode()).isEqualTo(ResponseCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("findByNickname Success")
    void findByNickname() {
        // given
        User user =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        "profileImage",
                        "blogName",
                        "refreshToken");
        String nickname = "nickname";
        when(userRepository.findByNicknameAndIsDeleted(nickname, false)).thenReturn(Optional.of(user));

        // when
        User findUser = userService.findByNickname(nickname);

        // then
        assertThat(findUser).isEqualTo(user);
    }

    @Test
    @DisplayName("findByNickname Fail - 존재하지 않는 유저")
    void findByNicknameFailUserNotFound() {
        // given
        String nickname = "nickname";
        when(userRepository.findByNicknameAndIsDeleted(nickname, false)).thenReturn(Optional.empty());

        // when
        NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> userService.findByNickname(nickname));

        // then
        assertThat(notFoundException.getResponseCode()).isEqualTo(ResponseCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("findByEmail Success")
    void findByEmail() {
        // given
        User user =
                new User(
                        "1",
                        "name",
                        "password",
                        "email",
                        null,
                        null,
                        Role.USER,
                        LocalDateTime.now(),
                        "nickname",
                        "profileImage",
                        "blogName",
                        "refreshToken");
        String email = "email";
        when(userRepository.findByEmailAndIsDeleted(email, false)).thenReturn(Optional.of(user));

        // when
        User findUser = userService.findByEmail(email);

        // then
        assertThat(findUser).isEqualTo(user);
    }

    @Test
    @DisplayName("findByEmail Fail - 존재하지 않는 유저")
    void findByEmailFailUserNotFound() {
        // given
        String email = "email";
        when(userRepository.findByEmailAndIsDeleted(email, false)).thenReturn(Optional.empty());

        // when
        NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> userService.findByEmail(email));

        // then
        assertThat(notFoundException.getResponseCode()).isEqualTo(ResponseCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("issueEmailCode Success - 이미 발급된 인증코드가 있을 때 && 아직 인증 완료 안된 이메일")
    void issueEmailCodeAlreadyExistAndNotVerified() {
        // given
        String email = "email";
        String emailCode = "123456";
        EmailCodeRequest request = new EmailCodeRequest(email);
        when(emailJoinCacheService.findEmailCode(email)).thenReturn(emailCode);
        when(emailJoinCacheService.findVerifiedEmail(email)).thenReturn(null);
        when(userRepository.findByEmailAndIsDeleted(email, false)).thenReturn(Optional.empty());

        // when
        userService.issueEmailCode(request);

        // then
        verify(emailJoinCacheService, times(1)).evictEmailCode(request.getEmail());
        verify(emailJoinCacheService, times(0)).evictVerifiedEmail(request.getEmail());
        verify(mailService, times(1)).sendMail(anyString(), anyString(), anyString());
        //        verify(emailJoinCacheService.cacheEmailCode(email, "654321"), times(1));
    }

    @Test
    @DisplayName("issueEmailCode Success - 이미 발급된 인증코드가 있을 때 && 이미 인증 완료된 이메일")
    void issueEmailCodeAlreadyExistAndAlreadyVerified() {
        // given
        String email = "email";
        String emailCode = "123456";
        EmailCodeRequest request = new EmailCodeRequest(email);
        when(emailJoinCacheService.findEmailCode(email)).thenReturn(emailCode);
        when(emailJoinCacheService.findVerifiedEmail(email)).thenReturn(email);
        when(userRepository.findByEmailAndIsDeleted(email, false)).thenReturn(Optional.empty());

        // when
        userService.issueEmailCode(request);

        // then
        verify(emailJoinCacheService, times(1)).evictEmailCode(request.getEmail());
        verify(emailJoinCacheService, times(1)).evictVerifiedEmail(request.getEmail());
        verify(mailService, times(1)).sendMail(anyString(), anyString(), anyString());
        //        verify(emailJoinCacheService.cacheEmailCode(anyString(), anyString()), times(1));
    }

    @Test
    @DisplayName("issueEmailCode Success - 발급된 인증코드가 없을 때 && 아직 인증 완료 안된 이메일")
    void issueEmailCodeNotExistAndNotVerified() {
        // given
        String email = "email";
        EmailCodeRequest request = new EmailCodeRequest(email);
        when(emailJoinCacheService.findEmailCode(email)).thenReturn(null);
        when(emailJoinCacheService.findVerifiedEmail(email)).thenReturn(null);
        when(userRepository.findByEmailAndIsDeleted(email, false)).thenReturn(Optional.empty());

        // when
        userService.issueEmailCode(request);

        // then
        verify(emailJoinCacheService, times(0)).evictEmailCode(request.getEmail());
        verify(emailJoinCacheService, times(0)).evictVerifiedEmail(request.getEmail());
        verify(mailService, times(1)).sendMail(anyString(), anyString(), anyString());
        //        verify(emailJoinCacheService.cacheEmailCode(anyString(), anyString()), times(1));
    }

    @Test
    @DisplayName("issueEmailCode Success - 발급된 인증코드가 없을 때 && 이미 인증 완료된 이메일")
    void issueEmailCodeNotExistAndAlreadyVerified() {
        // given
        String email = "email";
        EmailCodeRequest request = new EmailCodeRequest(email);
        when(emailJoinCacheService.findEmailCode(email)).thenReturn(null);
        when(emailJoinCacheService.findVerifiedEmail(email)).thenReturn(email);
        when(userRepository.findByEmailAndIsDeleted(email, false)).thenReturn(Optional.empty());

        // when
        userService.issueEmailCode(request);

        // then
        verify(emailJoinCacheService, times(0)).evictEmailCode(request.getEmail());
        verify(emailJoinCacheService, times(1)).evictVerifiedEmail(request.getEmail());
        verify(mailService, times(1)).sendMail(anyString(), anyString(), anyString());
        //        verify(emailJoinCacheService.cacheEmailCode(anyString(), anyString()), times(1));
    }

    @Test
    @DisplayName("verifyEmail Fail - 이미 가입된 이메일")
    void issueEmailCodeFailEmailAlreadyExist() {
        // given
        User user = new User();
        String email = "email";
        EmailCodeRequest request = new EmailCodeRequest(email);
        when(userRepository.findByEmailAndIsDeleted(email, false)).thenReturn(Optional.of(user));

        // when
        InvalidInputException invalidInputException =
                assertThrows(InvalidInputException.class, () -> userService.issueEmailCode(request));

        // then
        assertThat(invalidInputException.getResponseCode()).isEqualTo(ResponseCode.EMAIL_ALREADY_EXIST);
    }

    @Test
    @DisplayName("validateEmailCode - 캐시에 저장된 이메일 코드 존재 && 요청한 이메일 코드와 일치")
    void validateEmailCodeExistCodeAndCorrect() {
        // given
        String email = "email";
        String emailCode = "123456";
        EmailCodeValidationRequest request = new EmailCodeValidationRequest(email, emailCode);
        when(emailJoinCacheService.findEmailCode(email)).thenReturn(emailCode);

        // when
        EmailCodeValidationResponse emailCodeValidationResponse =
                userService.validateEmailCode(request);

        // then
        verify(emailJoinCacheService, times(1)).evictEmailCode(email);
        verify(emailJoinCacheService, times(1))
                .cacheVerifiedEmail(request.getEmail(), request.getCode());
        assertThat(emailCodeValidationResponse.isValid()).isTrue();
    }

    @Test
    @DisplayName("validateEmailCode - 캐시에 저장된 이메일 코드 존재 && 요청한 이메일 코드와 불일치")
    void validateEmailCodeExistCodeAndIncorrect() {
        // given
        String email = "email";
        String emailCode = "123456";
        EmailCodeValidationRequest request = new EmailCodeValidationRequest(email, emailCode);
        when(emailJoinCacheService.findEmailCode(email)).thenReturn("654321");

        // when
        EmailCodeValidationResponse emailCodeValidationResponse =
                userService.validateEmailCode(request);

        // then
        verify(emailJoinCacheService, times(0)).evictEmailCode(email);
        verify(emailJoinCacheService, times(0))
                .cacheVerifiedEmail(request.getEmail(), request.getCode());
        assertThat(emailCodeValidationResponse.isValid()).isFalse();
    }

    @Test
    @DisplayName("validateEmailCode - 캐시에 이메일 코드 존재하지 않음")
    void validateEmailCodeNotExistCode() {
        // given
        String email = "email";
        String emailCode = "123456";
        EmailCodeValidationRequest request = new EmailCodeValidationRequest(email, emailCode);
        when(emailJoinCacheService.findEmailCode(email)).thenReturn(null);

        // when
        EmailCodeValidationResponse emailCodeValidationResponse =
                userService.validateEmailCode(request);

        // then
        verify(emailJoinCacheService, times(0)).evictEmailCode(email);
        verify(emailJoinCacheService, times(0))
                .cacheVerifiedEmail(request.getEmail(), request.getCode());
        assertThat(emailCodeValidationResponse.isValid()).isFalse();
    }

    @Test
    @DisplayName("joinByEmail Success")
    void joinByEmail() {
        // given
        EmailJoinRequest request = new EmailJoinRequest("email@gmail.com", "password", "123456");
        when(userRepository.findByEmailAndIsDeleted(request.getEmail(), false))
                .thenReturn(Optional.empty());
        when(emailJoinCacheService.findVerifiedEmail(request.getEmail())).thenReturn(request.getCode());

        // when
        userService.joinByEmail(request);

        // then
        verify(userRepository, times(1)).save(any(User.class));
        verify(bcryptPasswordEncoder, times(1)).encode("password_email");
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailJoinCacheService, times(1)).evictVerifiedEmail(request.getEmail());
    }

    @Test
    @DisplayName("joinByEmail Fail - 이미 가입된 이메일")
    void joinByEmailFailEmailAlreadyExist() {
        // given
        EmailJoinRequest request = new EmailJoinRequest("email@gmail.com", "password", "123456");
        when(userRepository.findByEmailAndIsDeleted(request.getEmail(), false))
                .thenReturn(Optional.of(new User()));

        // when
        InvalidInputException invalidInputException =
                assertThrows(InvalidInputException.class, () -> userService.joinByEmail(request));

        // then
        assertThat(invalidInputException.getResponseCode()).isEqualTo(ResponseCode.EMAIL_ALREADY_EXIST);
    }

    @Test
    @DisplayName("joinByEmail Fail - 인증되지 않은 이메일")
    void joinByEmailFailNotVerifiedEmail() {
        // given
        EmailJoinRequest request = new EmailJoinRequest("email@gmail.com", "password", "123456");
        when(userRepository.findByEmailAndIsDeleted(request.getEmail(), false))
                .thenReturn(Optional.empty());
        when(emailJoinCacheService.findVerifiedEmail(request.getEmail())).thenReturn(null);

        // when
        InvalidInputException invalidInputException =
                assertThrows(InvalidInputException.class, () -> userService.joinByEmail(request));

        // then
        assertThat(invalidInputException.getResponseCode()).isEqualTo(ResponseCode.EMAIL_NOT_VERIFIED);
    }

    @Test
    @DisplayName("joinByEmail Fail - 인증코드 불일치")
    void joinByEmailFailCodeIncorrect() {
        // given
        EmailJoinRequest request = new EmailJoinRequest("email@gmail.com", "password", "123456");
        when(userRepository.findByEmailAndIsDeleted(request.getEmail(), false))
                .thenReturn(Optional.empty());
        when(emailJoinCacheService.findVerifiedEmail(request.getEmail())).thenReturn("654321");

        // when
        InvalidInputException invalidInputException =
                assertThrows(InvalidInputException.class, () -> userService.joinByEmail(request));

        // then
        assertThat(invalidInputException.getResponseCode())
                .isEqualTo(ResponseCode.EMAIL_CODE_NOT_MATCH);
    }

    @Test
    @DisplayName("validateEmail - 유일한 이메일")
    void validateEmailValid() {
        // given
        String email = "email";

        // when
        ValidationEmailResponse validationEmailResponse = userService.validateEmail(email);

        // then
        assertThat(validationEmailResponse.isDuplicated()).isFalse();
    }

    @Test
    @DisplayName("validateEmail - 중복 이메일")
    void validateEmailInvalid() {
        // given
        String email = "email";
        when(userRepository.existsByEmailAndIsDeleted(email, false)).thenReturn(true);

        // when
        ValidationEmailResponse validationEmailResponse = userService.validateEmail(email);

        // then
        assertThat(validationEmailResponse.isDuplicated()).isTrue();
    }
}
