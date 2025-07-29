package com.hb.cda.examapi.business;

import com.hb.cda.examapi.business.pojo.Balance;
import com.hb.cda.examapi.business.pojo.Debt;
import com.hb.cda.examapi.controller.dto.AccountDTO;
import com.hb.cda.examapi.controller.dto.AccountEntryDTO;
import com.hb.cda.examapi.controller.dto.DebtDTO;
import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.User;

import java.util.List;
import java.util.Map;

public interface AccountBusiness {
    List<Balance> calculateBalances(String accountId);
    List<Debt> calculateDebts(String accountId);
    List<Account> getAccountsByUser(User user);

}
