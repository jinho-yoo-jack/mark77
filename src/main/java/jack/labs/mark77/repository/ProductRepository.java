package jack.labs.mark77.repository;

import jack.labs.mark77.entity.Cart;
import jack.labs.mark77.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {
}
