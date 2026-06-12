package com.hb.cda.examapi.business;

import com.hb.cda.examapi.business.pojo.Balance;
import com.hb.cda.examapi.business.pojo.Debt;
import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.User;
import java.util.List;

public interface AccountBusiness {
    List<Balance> calculateBalances(String accountId);
    List<Debt> calculateDebts(String accountId);
    List<Account> getAccountsByUser(User user);

}
