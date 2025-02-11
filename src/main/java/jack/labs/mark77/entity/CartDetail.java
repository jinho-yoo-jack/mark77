package jack.labs.mark77.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cart_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CartDetail {
    @Id
    private int id;

    @Column
    private String size;

    @Column
    private String quantity;

    @OneToOne
    @JoinColumn(name = "id")
    private Product products;

}
