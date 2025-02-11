package jack.labs.mark77.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "cart_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cart_id")
    private long cartId;

    @Column
    private String size;

    @Column
    private long quantity;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product products;

}
