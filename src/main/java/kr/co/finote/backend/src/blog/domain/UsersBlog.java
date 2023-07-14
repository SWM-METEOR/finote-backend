package kr.co.finote.backend.src.blog.domain;

import javax.persistence.*;
import kr.co.finote.backend.global.entity.BaseEntity;
import kr.co.finote.backend.src.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
public class UsersBlog extends BaseEntity {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @Column(unique = true)
    private String url;

    @Builder
    public UsersBlog(User user, String name, String url) {
        this.user = user;
        this.name = name;
        this.url = url;
    }
}
