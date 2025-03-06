package jack.labs.mark77.entity.tricount;

import jack.labs.mark77.dto.tri_count.CreateSettlement;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "SETTLEMENT")
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "settlement")
    private List<SettlementUsers> joinedUsers;

    public static Settlement of(CreateSettlement createSettlement) {
        return Settlement.builder()
                .name(createSettlement.getSettlementName())
                .build();
    }
}
