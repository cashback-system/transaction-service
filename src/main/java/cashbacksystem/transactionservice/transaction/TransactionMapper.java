package cashbacksystem.transactionservice.transaction;

import cashbacksystem.transfer.transaction.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper {

    /**
     * Преобразование dto в сущность.
     *
     * @param dto - dto-объект
     * @return сущность с проставленными полями
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cardId", source = "dto.card.id")
    @Mapping(target = "categoryId", source = "dto.category.id")
    public abstract TransactionEntity convert(TransactionDTO dto);
}
