package jack.labs.mark77.dto.tri_count;

import lombok.*;

@Getter
@RequiredArgsConstructor
@Builder
public class ExpenseResult {
    private final String settlementName;
    private final String expenseName; // 지출명
    private final String expenseUserName; // 지출한 사람
    private final String expenseAmount;
    private final String expensedDate;

}
