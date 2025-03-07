package jack.labs.mark77.dto.tri_count;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReqRegisterExpense {
    private long settlementId;
    private String amount;
    private String expenseDate;
}
