package jack.labs.mark77.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    private String id;

    private String password;

    @Column(name = "nick_name", nullable = false)
    private String nickname;

    private String role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id") // CART 테이블의 USER_ID(FK)
    private Cart cart;

}
