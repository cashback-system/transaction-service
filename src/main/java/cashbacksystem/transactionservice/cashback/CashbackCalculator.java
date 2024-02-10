package cashbacksystem.transactionservice.cashback;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Класс для расчета суммы кэшбека.
 */
@Component
public class CashbackCalculator {

    /**
     * Рассчитать кэшбек.
     *
     * @param cardCashbackPercent     - процент кэшбека по карте
     * @param categoryCashbackPercent - процент кэшбека по категории
     * @param transactionSum          - сумма транзакции
     * @return сумма кэшбека
     */
    public BigDecimal calculateCashback(BigDecimal cardCashbackPercent,
                                        BigDecimal categoryCashbackPercent,
                                        BigDecimal transactionSum) {
        BigDecimal totalCashbackPercent = cardCashbackPercent
            .add(categoryCashbackPercent);

        return transactionSum
            .multiply(totalCashbackPercent)
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);
    }
}
