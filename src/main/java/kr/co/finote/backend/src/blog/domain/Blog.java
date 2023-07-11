package kr.co.finote.backend.src.blog.domain;

import kr.co.finote.backend.global.entity.BaseEntity;
import kr.co.finote.backend.src.user.domain.User;
import lombok.Builder;

import javax.persistence.*;

@Entity
public class Blog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    private String url;

    @Builder
    public Blog(User user,
                String name,
                String url) {
        this.user = user;
        this.name = name;
        this.url = url;
    }

}
