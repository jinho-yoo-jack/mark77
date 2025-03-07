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

    @Column(name = "created_at")
    private LocalDate createdAt;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "settlement_id")
    private Settlement settlement;

    public static Expense of(Settlement s, User u, BigDecimal a, String d) {
        return Expense.builder()
                .user(u)
                .amount(a)
                .createdAt(convertStringToDate(d))
                .settlement(s)
                .build();
    }

    private static LocalDate convertStringToDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }
}
