package cashbacksystem.transactionservice.transaction;

import cashbacksystem.transactionservice.cashback.CashbackCalculator;
import cashbacksystem.transfer.api.CardApi;
import cashbacksystem.transfer.api.CategoryApi;
import cashbacksystem.transfer.card.CardDTO;
import cashbacksystem.transfer.cashback.CashbackDTO;
import cashbacksystem.transfer.category.CategoryDTO;
import cashbacksystem.transfer.transaction.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final CardApi cardApi;
    private final CategoryApi categoryApi;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CashbackCalculator cashbackCalculator;

    /**
     * Рассчитать кэшбек по транзакции.
     *
     * @param transaction - данные транзакции
     * @return данные о кэшбеке
     */
    public CashbackDTO calculateCashback(TransactionDTO transaction) {
        CardDTO card = cardApi.getCardById(transaction.card().id());
        CategoryDTO category = categoryApi.getCategoryById(transaction.category().id());
        TransactionEntity entity = transactionMapper.convert(transaction);
        transactionRepository.save(entity);

        return CashbackDTO.builder()
            .withCashbackValue(cashbackCalculator.calculateCashback(
                card.cashbackPercent(), category.cashbackPercent(), transaction.amount()
            ))
            .build();
    }
}
