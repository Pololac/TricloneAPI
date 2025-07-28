package com.hb.cda.examapi.business;

import com.hb.cda.examapi.controller.dto.AccountDTO;
import com.hb.cda.examapi.controller.dto.AccountEntryDTO;
import com.hb.cda.examapi.controller.dto.DebtDTO;

import java.util.List;

public interface AccountBusiness {
    List<AccountEntryDTO> calculateBalances(String accountId);
    List<DebtDTO> calculateDebts(String accountId);
    List<AccountDTO> getAccountsByUser(String userId);

}
