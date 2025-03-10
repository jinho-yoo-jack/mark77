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

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

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
    public List<ExpenseResult> register(RegisterExpense expenseInfo) {
        Settlement settlement = settlementRepository.findById(expenseInfo.getSettlementId()).orElseThrow();
        User user = userRepository.findById(jwtService.getUserId()).orElseThrow();
        expenseRepository.save(Expense.of(settlement, user, expenseInfo.getAmount(), expenseInfo.getExpenseDate(), expenseInfo.getExpenseName()));
        return settlement.getExpenses().stream().map(e -> ExpenseResult.builder()
                .settlementName(e.getSettlement().getName())
                .expenseUserName(e.getUser().getNickname())
                .expenseAmount(e.getAmount().toString())
                .expensedDate(e.getExpensedAt().toString())
                .expenseName(e.getExpenseName())
                .build()
        ).toList();
    }

    @Transactional
    public List<RespSettlementResult> settlement(long settlementId) {
        Settlement settlement = settlementRepository.findById(settlementId).orElseThrow();
        List<Expense> expenses = settlement.getExpenses();
        BigDecimal totalAmount = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal perPersonAmount = totalAmount.divide(BigDecimal.valueOf(expenses.size()), 2, BigDecimal.ROUND_HALF_UP);

        Map<String, BigDecimal> groupByUserAmount = expenses.stream().collect(
                Collectors.groupingBy(e -> e.getUser().getId(), Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));

        Queue<Map.Entry<String, BigDecimal>> receivers = new LinkedBlockingQueue<>();
        Queue<Map.Entry<String, BigDecimal>> senders = new LinkedBlockingQueue<>();
        for (String key : groupByUserAmount.keySet()) {
            BigDecimal diff = groupByUserAmount.get(key).subtract(perPersonAmount);
            if (diff.signum() > 0) receivers.add(Map.entry(key, diff));
            else senders.add(Map.entry(key, diff));
        }

        List<RespSettlementResult> result = new ArrayList<>();
        while (!receivers.isEmpty()) {
            Map.Entry<String, BigDecimal> receiver = receivers.poll();
            String receiverUserId = receiver.getKey();
            BigDecimal receiveAmount = receiver.getValue();

            Map.Entry<String, BigDecimal> sender = senders.poll();
            do {
                String senderUserId = sender.getKey();
                BigDecimal senderAmount = sender.getValue().abs();
                if (equal(receiveAmount, senderAmount)) {
                    result.add(RespSettlementResult.builder()
                            .senderUserName(senderUserId)
                            .senderUserId(senderUserId)
                            .sendAmount(senderAmount.toString())
                            .receiverUserName(receiverUserId)
                            .receiverUserId(receiverUserId)
                            .build());
                    break;
                } else if (bigger(receiveAmount, senderAmount)) {
                    result.add(RespSettlementResult.builder()
                            .senderUserName(senderUserId)
                            .senderUserId(senderUserId)
                            .sendAmount(senderAmount.toString())
                            .receiverUserName(receiverUserId)
                            .receiverUserId(receiverUserId)
                            .build());

                    receiveAmount = receiveAmount.subtract(senderAmount);
                    if (receiveAmount.compareTo(BigDecimal.ZERO) == 0) break;
                    sender = senders.poll();
                } else {
                    result.add(RespSettlementResult.builder()
                            .senderUserName(senderUserId)
                            .senderUserId(senderUserId)
                            .sendAmount(receiveAmount.toString())
                            .receiverUserName(receiverUserId)
                            .receiverUserId(receiverUserId)
                            .build());

                    senders.add(Map.entry(senderUserId, senderAmount.subtract(receiveAmount)));
                    break;
                }
            } while (!senders.isEmpty());

        }

        return result;

    }

    private boolean equal(BigDecimal r, BigDecimal s) {
        return r.compareTo(s) == 0; // r == s
    }

    private boolean bigger(BigDecimal r, BigDecimal s) {
        return r.compareTo(s) > 0; // r > s
    }

    private boolean less(BigDecimal r, BigDecimal s) {
        return r.compareTo(s) < 0; // r < s
    }

}
