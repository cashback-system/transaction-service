package cashbacksystem.transactionservice.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository
    extends JpaRepository<TransactionEntity, UUID> {

    /**
     * Найти все транзакции по карте и временному промежутку.
     *
     * @param cardId - идентификатор карты
     * @param start  - минимальное время транзакции
     * @param end    - максимальное время транзакции
     * @return список транзакций, удовлетворяющих параметрам
     */
    List<TransactionEntity> findAllByCardIdAndTimeBetween(UUID cardId,
                                                          ZonedDateTime start,
                                                          ZonedDateTime end);
}
