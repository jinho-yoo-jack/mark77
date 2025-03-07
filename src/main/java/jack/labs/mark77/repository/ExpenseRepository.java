package jack.labs.mark77.repository;

import jack.labs.mark77.entity.tricount.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
