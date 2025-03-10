package jack.labs.mark77.entity.tricount;

import jack.labs.mark77.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Table(name = "EXPENSE")
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expense_name")
    private String expenseName;

    @Column(name = "expensed_at")
    private LocalDate expensedAt;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "settlement_id")
    private Settlement settlement;

    public static Expense of(Settlement s, User u, BigDecimal a, String d, String e) {
        return Expense.builder()
                .user(u)
                .amount(a)
                .expensedAt(convertStringToDate(d))
                .expenseName(e)
                .settlement(s)
                .build();
    }

    private static LocalDate convertStringToDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }
}
