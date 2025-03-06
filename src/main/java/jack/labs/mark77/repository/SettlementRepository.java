package jack.labs.mark77.repository;

import jack.labs.mark77.entity.tricount.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
