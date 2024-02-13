package cashbacksystem.transactionservice.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Сущность агитационные материалы.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "transaction")
@Accessors(chain = true)
public class TransactionEntity {

    /**
     * Идентификатор.
     */
    @Id
    @UuidGenerator
    private UUID id;

    /**
     * Время.
     */
    private ZonedDateTime time;

    /**
     * Карта.
     */
    private UUID cardId;

    /**
     * Категория.
     */
    private UUID categoryId;

    /**
     * Сумма.
     */
    private BigDecimal amount;
}
