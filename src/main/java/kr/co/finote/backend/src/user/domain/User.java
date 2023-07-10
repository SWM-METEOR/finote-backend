package kr.co.finote.backend.src.user.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import kr.co.finote.backend.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "USERS")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String email;

    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime lastLoginDate;

    @Builder
    public User(
            String username,
            String password,
            String email,
            String provider,
            String providerId,
            Role role,
            LocalDateTime lastLoginDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.lastLoginDate = lastLoginDate;
    }
}
