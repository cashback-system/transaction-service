package cashbacksystem.transactionservice.unit.cashback;

import cashbacksystem.transactionservice.cashback.CashbackCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Unit-тесты для расчета кэшбека.
 */
public class CashbackCalculatorTest {
    private final CashbackCalculator cashbackCalculator =
        new CashbackCalculator();

    @Test
    @DisplayName("Тест кэшбэка при нулевой сумме транзакции")
    public void testZeroCashback() {
        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN),
            cashbackCalculator.calculateCashback(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
            ));

        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN),
            cashbackCalculator.calculateCashback(
                BigDecimal.ONE,
                BigDecimal.ZERO,
                BigDecimal.ZERO
            ));

        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN) ,
            cashbackCalculator.calculateCashback(
                BigDecimal.ZERO,
                BigDecimal.ONE,
                BigDecimal.ZERO
            ));
    }

    @Test
    @DisplayName("Тест расчета кэшбэка только по карте")
    public void testOnlyCardCashback() {
        Assertions.assertEquals(BigDecimal.valueOf(66.22),
            cashbackCalculator.calculateCashback(
                BigDecimal.valueOf(5),
                BigDecimal.ZERO,
                BigDecimal.valueOf(1324.45)
            ));
    }

    @Test
    @DisplayName("Тест расчета кэшбэка только по категории")
    public void testOnlyCategoryCashback() {
        Assertions.assertEquals(BigDecimal.valueOf(409.47),
            cashbackCalculator.calculateCashback(
                BigDecimal.ZERO,
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(5849.62)
            ));
    }

    @Test
    @DisplayName("Тест расчета кэшбэка по карте и по категории")
    public void testCardAndCategoryCashback() {
        Assertions.assertEquals(BigDecimal.valueOf(1558.55),
            cashbackCalculator.calculateCashback(
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(12987.99)
            ));
    }
}
