package jack.labs.mark77.service.tricount;

import jack.labs.mark77.dto.tri_count.*;
import jack.labs.mark77.entity.User;
import jack.labs.mark77.entity.tricount.Expense;
import jack.labs.mark77.entity.tricount.Settlement;
import jack.labs.mark77.entity.tricount.SettlementUsers;
import jack.labs.mark77.repository.ExpenseRepository;
import jack.labs.mark77.repository.SettlementRepository;
import jack.labs.mark77.repository.SettlementUsersRepository;
import jack.labs.mark77.repository.UserRepository;
import jack.labs.mark77.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementService {
    private final SettlementRepository settlementRepository;
    private final SettlementUsersRepository settlementUsersRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public Settlement create(CreateSettlement settlement) {
        return settlementRepository.save(Settlement.of(settlement));
    }

    @Transactional
    public RespJoinSettlement join(JoinSettlement joinSettlement) {
        Settlement settlement = settlementRepository.findById(joinSettlement.getSettlementId()).orElseThrow(
                () -> new IllegalArgumentException("Settlement is not found")
        );

        SettlementUsers joinedUser = settlementUsersRepository.save(
                SettlementUsers.builder()
                        .settlement(settlement)
                        .userId(jwtService.getUserId())
                        .build());

        return RespJoinSettlement.of(joinedUser);
    }

    @Transactional
    public List<ExpenseInfo> register(RegisterExpense expenseInfo) {
        Settlement settlement = settlementRepository.findById(expenseInfo.getSettlementId()).orElseThrow();
        User user = userRepository.findById(jwtService.getUserId()).orElseThrow();
        expenseRepository.save(Expense.of(settlement, user, expenseInfo.getAmount(), expenseInfo.getExpenseDate()));
        return settlement.getExpenses().stream().map(e -> ExpenseInfo.builder()
                .settlementName(e.getSettlement().getName())
                .expenseUserName(e.getUser().getNickname())
                .expenseAmount(e.getAmount().toString())
                .expensedDate(e.getCreatedAt().toString())
                .build()
        ).toList();

    }

}
