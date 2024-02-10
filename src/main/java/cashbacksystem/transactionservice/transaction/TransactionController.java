package cashbacksystem.transactionservice.transaction;

import cashbacksystem.transfer.cashback.CashbackDTO;
import cashbacksystem.transfer.transaction.TransactionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
