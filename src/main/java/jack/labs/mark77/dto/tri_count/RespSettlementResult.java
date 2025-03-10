package jack.labs.mark77.dto.tri_count;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RespSettlementResult {
    private final String senderUserId;
    private final String senderUserName;
    private final String sendAmount;
    private final String receiverUserId;
    private final String receiverUserName;
}
