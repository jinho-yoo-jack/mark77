package jack.labs.mark77.dto.tri_count;

import lombok.*;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
@Builder
public class RegisterExpense {
    private final long settlementId;
    private final BigDecimal amount;
    private final String expenseDate;
    private final String expenseName;

    public static RegisterExpense of(ReqRegisterExpense requestMessage) {
        return RegisterExpense.builder()
                .settlementId(requestMessage.getSettlementId())
                .amount(new BigDecimal(requestMessage.getAmount()))
                .expenseDate(requestMessage.getExpenseDate())
                .expenseName(requestMessage.getExpenseName())
                .build();
    }
}
