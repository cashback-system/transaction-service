package cashbacksystem.transactionservice.transaction;

import cashbacksystem.transfer.api.CardApi;
import cashbacksystem.transfer.api.CategoryApi;
import cashbacksystem.transfer.transaction.TransactionDTO;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {
    CardApi.class,
    CategoryApi.class
})
@Setter
@Accessors(chain = true)
public abstract class TransactionMapper {

    @Autowired
    protected CardApi cardApi;

    @Autowired
    protected CategoryApi categoryApi;

    /**
     * Преобразование сущности в dto.
     *
     * @param dto    - dto
     * @param entity - сущность
     * @return dto с проставленными полями
     */
    @Mapping(target = "card", ignore = true)
    @Mapping(target = "category", ignore = true)
    protected abstract TransactionDTO convertFromEntity(@MappingTarget TransactionDTO dto,
                                                        TransactionEntity entity);

    /**
     * Преобразование dto в сущность.
     *
     * @param dto - dto-объект
     * @return сущность с проставленными полями
     */
    @Mapping(target = "cardId", source = "dto.card.id")
    @Mapping(target = "categoryId", source = "dto.category.id")
    protected abstract TransactionEntity convertFromDto(TransactionDTO dto);

    /**
     * После маппинга из сущности, проставляем зависимости.
     *
     * @param dto    - dto
     * @param entity - сущность
     */
    @AfterMapping
    protected void afterMappingFromEntity(@MappingTarget TransactionDTO dto,
                                          TransactionEntity entity) {
        dto.setCard(cardApi.getCardById(entity.getCardId()));
        dto.setCategory(categoryApi.getCategoryById(entity.getCategoryId()));
    }
}
