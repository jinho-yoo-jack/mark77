package jack.labs.mark77.dto.tri_count;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jack.labs.mark77.entity.tricount.Settlement;
import jack.labs.mark77.entity.tricount.SettlementUsers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RespJoinSettlement {
    private long settlementId;
    private String settlementName;
    private String userId;

    public static RespJoinSettlement of(SettlementUsers settlementUsers) {
        return RespJoinSettlement.builder()
            .settlementId(settlementUsers.getSettlement().getId())
            .settlementName(settlementUsers.getSettlement().getName())
            .userId(settlementUsers.getUserId())
            .build();
    }
}
