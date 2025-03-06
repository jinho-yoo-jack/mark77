package jack.labs.mark77.dto.tri_count;

import lombok.*;

@Getter
@RequiredArgsConstructor
@Builder
public class CreateSettlement {
    private final String settlementName;

    public static CreateSettlement of(ReqCreateSettlement request) {
        return CreateSettlement.builder()
                .settlementName(request.getSettlementName())
                .build();
    }
}
