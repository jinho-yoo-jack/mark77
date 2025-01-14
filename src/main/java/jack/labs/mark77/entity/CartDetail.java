package jack.labs.mark77.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cart_detail")
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private String id;

    @Column
    private String size;

    @Column
    private String quantity;

    @OneToMany
    @JoinColumn(name = "product_id")
    private List<Product> products;

}
