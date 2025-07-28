package com.hb.cda.examapi.controller.dto.mapper;

import com.hb.cda.examapi.controller.dto.PostExpenseDTO;
import com.hb.cda.examapi.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExpenseMapper {
    @Mapping(source = "payer.id", target = "payerId")
    PostExpenseDTO convertToDTO(Expense expense);

    @Mapping(target = "id", ignore = true)       // on crée, donc pas d’ID à copier
    @Mapping(target = "payer", ignore = true)    // on l’injecte dans la couche Business
    @Mapping(target = "account", ignore = true)  // idem pour l’account
    Expense convertFromPost(PostExpenseDTO dto);
}
