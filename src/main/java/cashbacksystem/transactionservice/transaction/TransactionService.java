package cashbacksystem.transactionservice.transaction;

import cashbacksystem.transactionservice.cashback.CashbackCalculator;
import cashbacksystem.transfer.api.CardApi;
import cashbacksystem.transfer.api.CategoryApi;
import cashbacksystem.transfer.card.CardDTO;
import cashbacksystem.transfer.cashback.CashbackDTO;
import cashbacksystem.transfer.category.CategoryDTO;
import cashbacksystem.transfer.transaction.TransactionDTO;
import cashbacksystem.transfer.transaction.TransactionSearchParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        CardDTO card = cardApi.getCardById(transaction.getCard().id());
        CategoryDTO category = categoryApi.getCategoryById(transaction.getCategory().id());
        TransactionEntity entity = transactionMapper.convertFromDto(transaction);
        transactionRepository.save(entity);

        return CashbackDTO.builder()
            .withCashbackValue(cashbackCalculator.calculateCashback(
                card.cashbackPercent(), category.cashbackPercent(), transaction.getAmount()
            ))
            .build();
    }

    /**
     * Поиск транзакций по параметрам.
     *
     * @param params - параметры поиска
     * @return найденные транзакции
     */
    public List<TransactionDTO> searchByParams(TransactionSearchParams params) {
        return transactionRepository
            .findAllByCardIdAndTimeBetween(params.card().id(), params.startTime(), params.endTime())
            .stream().map(entity -> transactionMapper.convertFromEntity(new TransactionDTO(), entity))
            .collect(Collectors.toList());
    }
}
