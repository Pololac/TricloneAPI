package com.hb.cda.examapi.controller.dto.mapper;

import com.hb.cda.examapi.controller.dto.AccountDTO;
import com.hb.cda.examapi.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    AccountDTO convertToDto(Account account);
    Account convertToEntity(AccountDTO dto);
}
