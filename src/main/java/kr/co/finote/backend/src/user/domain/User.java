package kr.co.finote.backend.src.user.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import kr.co.finote.backend.global.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
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

    private String profileImageUrl;

    private String blogName;
    private String blogUrl;
}
