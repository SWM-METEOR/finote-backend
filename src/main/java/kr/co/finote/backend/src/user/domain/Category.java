package kr.co.finote.backend.src.user.domain;

import javax.persistence.*;
import kr.co.finote.backend.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    public static Category createCategory(User user, String name) {
        return Category.builder().user(user).name(name).build();
    }
}
