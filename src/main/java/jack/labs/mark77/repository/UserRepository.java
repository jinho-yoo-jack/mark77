package jack.labs.mark77.repository;

import jack.labs.mark77.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
