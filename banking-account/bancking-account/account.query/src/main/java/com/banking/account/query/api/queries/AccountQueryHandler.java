package com.banking.account.query.api.queries;

import com.banking.account.query.api.dto.EqualityType;
import com.banking.account.query.domain.AccountRepository;
import com.banking.account.query.domain.BankAccount;
import com.banking.cqrs.core.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountQueryHandler implements QueryHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> accounts = accountRepository.findAll();
        List<BaseEntity> accountList = new ArrayList<>();
        accounts.forEach(accountList::add);
        return accountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {

        var account = accountRepository.findById(query.getId());
        if (account.isPresent()) {
            List<BaseEntity> accountList = new ArrayList<>();
            accountList.add(account.get());
            return accountList;
        }
        return null;
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
        List<BaseEntity> accounts = query.getEqualityType() == EqualityType.GREATER_THAN
                ? accountRepository.findByBalanceGreaterThan(query.getBalance())
                : accountRepository.findByBalanceLessThan(query.getBalance());
        if (accounts != null) {
            return accounts;
        }

        return null;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        var accounts = accountRepository.findByAccountHolder(query.getAccountHolder());
        if (accounts.isPresent()) {
            List<BaseEntity> accountList = new ArrayList<>();
            accountList.add(accounts.get());
            return accountList;
        }
        return null;
    }
}
