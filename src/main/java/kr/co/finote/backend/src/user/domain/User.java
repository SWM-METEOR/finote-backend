package kr.co.finote.backend.src.user.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;
import kr.co.finote.backend.global.authentication.oauth.google.dto.response.GoogleUserInfo;
import kr.co.finote.backend.global.entity.BaseEntity;
import kr.co.finote.backend.src.user.dto.request.AdditionalInfoRequest;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    private String username;
    private String password;
    private String email;

    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime lastLoginDate;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false, length = 500)
    private String profileImageUrl;

    private String blogName;

    private String refreshToken;

    public static User newGoogleUser(
            GoogleUserInfo googleUserInfo, String randomNickname, LocalDateTime lastLoginDate) {
        return User.builder()
                .username(googleUserInfo.getName())
                .email(googleUserInfo.getEmail())
                .provider(SocialType.GOOGLE.getValue())
                .providerId(googleUserInfo.getId())
                .role(Role.USER)
                .lastLoginDate(lastLoginDate)
                .nickname(randomNickname)
                .profileImageUrl(
                        "https://finote-image-bucket.s3.ap-northeast-2.amazonaws.com/profile_image.png")
                .blogName(randomNickname)
                .build();
    }

    public static User newEmailUser(String email, String password, String randomNickname) {
        return User.builder()
                .email(email)
                .password(password)
                .username(null)
                .provider(null)
                .providerId(null)
                .role(Role.USER)
                .lastLoginDate(null)
                .nickname(randomNickname)
                .profileImageUrl(
                        "https://finote-image-bucket.s3.ap-northeast-2.amazonaws.com/profile_image.png")
                .blogName(randomNickname)
                .build();
    }

    public void updateLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public void updateAdditionalInfo(AdditionalInfoRequest additionalInfoRequest) {
        this.nickname = additionalInfoRequest.getNickname();
        this.blogName = additionalInfoRequest.getBlogName();
        if (!Objects.equals(additionalInfoRequest.getProfileImageUrl(), ""))
            this.profileImageUrl = additionalInfoRequest.getProfileImageUrl();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
