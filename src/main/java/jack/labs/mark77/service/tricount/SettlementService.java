package jack.labs.mark77.service.tricount;

import jack.labs.mark77.dto.tri_count.CreateSettlement;
import jack.labs.mark77.dto.tri_count.JoinSettlement;
import jack.labs.mark77.entity.tricount.Settlement;
import jack.labs.mark77.entity.tricount.SettlementUsers;
import jack.labs.mark77.repository.SettlementRepository;
import jack.labs.mark77.repository.SettlementUsersRepository;
import jack.labs.mark77.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public SettlementUsers join(JoinSettlement joinSettlement) {
        if (isExisted(joinSettlement.getSettlementId())) {
            String userId = jwtService.getUserId();
            return settlementUsersRepository.save(
                    SettlementUsers.builder()
                            .id(joinSettlement.getSettlementId())
                            .userId(userId)
                            .build());

        }
        throw new IllegalArgumentException("Settlement is not found");
    }

    private boolean isExisted(long settlementId) {
        return settlementRepository.findById(settlementId).isPresent();
    }
}
