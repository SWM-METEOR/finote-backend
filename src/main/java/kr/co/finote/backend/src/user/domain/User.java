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
    private String role;

    private String provider;
    private String providerId;

    private LocalDateTime lastLoginDate;

    @Builder
    public User(
            String username,
            String password,
            String email,
            String role,
            String provider,
            String providerId,
            LocalDateTime lastLoginDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.lastLoginDate = lastLoginDate;
    }
}
