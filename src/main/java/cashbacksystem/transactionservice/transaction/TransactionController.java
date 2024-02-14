package cashbacksystem.transactionservice.transaction;

import cashbacksystem.transfer.cashback.CashbackDTO;
import cashbacksystem.transfer.transaction.TransactionDTO;
import cashbacksystem.transfer.transaction.TransactionSearchParams;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * Рассчитать кэшбек по транзакции.
     *
     * @param transaction - данные транзакции
     * @return данные о кэшбеке
     */
    @PostMapping("/calculate-cashback")
    public CashbackDTO calculateCashback(
        @Valid @RequestBody TransactionDTO transaction
    ) {
        return transactionService.calculateCashback(transaction);
    }

    /**
     * Поиск транзакций по параметрам.
     *
     * @param transactionSearchParams - параметры поиска
     * @return найденные транзакции
     */
    @PostMapping("/search")
    public List<TransactionDTO> searchByParams(
        @Valid @RequestBody TransactionSearchParams transactionSearchParams
    ) {
        return transactionService.searchByParams(transactionSearchParams);
    }
}
