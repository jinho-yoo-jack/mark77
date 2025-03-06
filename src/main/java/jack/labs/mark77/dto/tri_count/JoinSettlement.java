package jack.labs.mark77.dto.tri_count;

import lombok.*;

@Getter
@AllArgsConstructor
public class JoinSettlement {
    private long settlementId;

    public static JoinSettlement of(long settlementId) {
        return new JoinSettlement(settlementId);
    }
}
