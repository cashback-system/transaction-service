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
import cashbacksystem.transfer.transaction.TransactionSearchParams;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Тест для проверки поиска транзакций.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(
    scripts = {
        "classpath:sql/transaction/create.sql"
    },
    config = @SqlConfig(encoding = "UTF-8")
)
@Sql(
    scripts = "classpath:sql/clear-all.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
    config = @SqlConfig(encoding = "UTF-8")
)
public class TransactionSearchTest {

    private final CardApi cardApi = Mockito.mock(CardApi.class);
    private final CategoryApi categoryApi = Mockito.mock(CategoryApi.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    private TransactionController transactionController;

    @BeforeEach
    public void init() {
        transactionMapper.setCardApi(cardApi).setCategoryApi(categoryApi);
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
    @DisplayName("Тест поиска транзакций по параметрам")
    public void testSearchByParams() {
        Mockito.when(cardApi.getCardById(Mockito.any()))
            .thenReturn(CardDTO.builder()
                .withId(UUID.fromString("b2cd25d5-a0eb-46a0-8fc3-d917fa33e0da"))
                .withCardType(CardType.DEFAULT)
                .withCashbackPercent(BigDecimal.ONE)
                .build());

        Mockito.when(categoryApi.getCategoryById(Mockito.any()))
            .thenReturn(CategoryDTO.builder()
                .withId(UUID.fromString("a2a4ef26-b49d-42e1-b11a-daec3c6fdca8"))
                .withName("Книги")
                .withCashbackPercent(BigDecimal.valueOf(3))
                .build());


        List<TransactionDTO> transactions = transactionController.searchByParams(TransactionSearchParams.builder()
            .withCard(CardDTO.builder()
                .withId(UUID.fromString("b2cd25d5-a0eb-46a0-8fc3-d917fa33e0da"))
                .build())
            .withStartTime(ZonedDateTime.of(2024, 2, 14, 22, 0, 0, 0, ZoneId.of("Europe/Moscow")))
            .withEndTime(ZonedDateTime.of(2024, 2, 14, 23, 50, 0, 0, ZoneId.of("Europe/Moscow")))
            .build());

        Assertions.assertEquals(2, transactions.size());
        Assertions.assertTrue(transactions.stream().map(TransactionDTO::getId)
            .toList().containsAll(List.of(UUID.fromString("f172c39e-ff12-4d8b-ba37-1ea30c3ee307"),
                UUID.fromString("5e6e71e3-1abc-473a-8d74-66c40efe2f19"))));
    }
}
