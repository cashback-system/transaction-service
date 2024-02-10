package cashbacksystem.transactionservice.cashback;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
        BigDecimal cardCashback = transactionSum.add(transactionSum.multiply(cardCashbackPercent));
        BigDecimal categoryCashback = transactionSum.add(transactionSum.multiply(categoryCashbackPercent));

        return cardCashback.add(categoryCashback);
    }
}
