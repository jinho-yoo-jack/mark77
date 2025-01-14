package jack.labs.mark77.repository;

import jack.labs.mark77.entity.Cart;
import jack.labs.mark77.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
    Optional<Cart> findByUserId(String userId);
}
