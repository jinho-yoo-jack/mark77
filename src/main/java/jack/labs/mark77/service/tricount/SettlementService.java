package jack.labs.mark77.service.tricount;

import jack.labs.mark77.dto.tri_count.CreateSettlement;
import jack.labs.mark77.dto.tri_count.JoinSettlement;
import jack.labs.mark77.dto.tri_count.RespJoinSettlement;
import jack.labs.mark77.entity.tricount.Settlement;
import jack.labs.mark77.entity.tricount.SettlementUsers;
import jack.labs.mark77.repository.SettlementRepository;
import jack.labs.mark77.repository.SettlementUsersRepository;
import jack.labs.mark77.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementService {
    private final SettlementRepository settlementRepository;
    private final SettlementUsersRepository settlementUsersRepository;
    private final JwtService jwtService;

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

}
