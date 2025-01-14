package jack.labs.mark77.repository;

import jack.labs.mark77.entity.Cart;
import jack.labs.mark77.entity.CartDetail;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartDetailRepository extends CrudRepository<CartDetail, UUID> {
}
