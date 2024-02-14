package cashbacksystem.transactionservice.integration.transaction;

import cashbacksystem.transactionservice.cashback.CashbackCalculator;
import cashbacksystem.transactionservice.transaction.TransactionController;
import cashbacksystem.transactionservice.transaction.TransactionMapper;
import cashbacksystem.transactionservice.transaction.TransactionRepository;
import cashbacksystem.transactionservice.transaction.TransactionService;
import cashbacksystem.transfer.api.CardApi;
import cashbacksystem.transfer.api.CategoryApi;
import cashbacksystem.transfer.card.CardDTO;
import cashbacksystem.transfer.card.CardType;
import cashbacksystem.transfer.category.CategoryDTO;
import cashbacksystem.transfer.transaction.TransactionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Тест для проверки расчета кэшбека по транзакции.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(
    scripts = "classpath:sql/clear-all.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
    config = @SqlConfig(encoding = "UTF-8")
)
public class TransactionCashbackCalculationTest {

    private final CardApi cardApi = Mockito.mock(CardApi.class);
    private final CategoryApi categoryApi = Mockito.mock(CategoryApi.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    private TransactionController transactionController;

    @BeforeEach
    public void init() {
        transactionController = new TransactionController(
            new TransactionService(
                cardApi,
                categoryApi,
                transactionRepository,
                transactionMapper,
                new CashbackCalculator()
            )
        );
    }

    @Test
    @DisplayName("Проверка вычисления кэшбэка по карте и категории")
    public void testCashbackCalculation() {
        Mockito.when(cardApi.getCardById(Mockito.any()))
            .thenReturn(CardDTO.builder()
                .withId(UUID.fromString("dbafa003-2e80-42b4-9562-3ba0be9f76cd"))
                .withCardType(CardType.DEFAULT)
                .withCashbackPercent(BigDecimal.ONE)
                .build());

        Mockito.when(categoryApi.getCategoryById(Mockito.any()))
            .thenReturn(CategoryDTO.builder()
                .withId(UUID.fromString("0abd517a-e5b2-4899-a72d-2ca1b3666a81"))
                .withName("Книги")
                .withCashbackPercent(BigDecimal.valueOf(3))
                .build());

        Assertions.assertEquals(BigDecimal.valueOf(799.99),
            transactionController.calculateCashback(TransactionDTO.builder()
                .withId(UUID.randomUUID())
                .withTime(ZonedDateTime.now())
                .withCard(cardApi.getCardById(UUID.randomUUID()))
                .withCategory(categoryApi.getCategoryById(UUID.randomUUID()))
                .withAmount(BigDecimal.valueOf(19999.99))
                .build()).cashbackValue());

        Assertions.assertEquals(1, transactionRepository.findAll().size());
    }
}
