package jack.labs.mark77.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id") // CART_DETAIL 테이블의 CART_ID(FK)
    private List<CartDetail> cartDetails;

}