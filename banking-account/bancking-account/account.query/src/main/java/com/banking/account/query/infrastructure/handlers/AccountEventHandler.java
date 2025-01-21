package com.banking.account.query.infrastructure.handlers;

import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawnEvent;
import com.banking.account.query.domain.AccountRepository;
import com.banking.account.query.domain.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler{
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        // Create a new bank account
        var bankAccount = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .createdDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();
        // Save the bank account
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        // Get the bank account
        var bankAccount = accountRepository.findById(event.getId());

        if (bankAccount.isEmpty()) {
            throw new IllegalArgumentException("Bank account not found");
        }
        // Get the current balance
        var currentBalance = bankAccount.get().getBalance();

        if (currentBalance + event.getAmount() < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Update the balance
        bankAccount.get().setBalance(currentBalance + event.getAmount());
        // Save the bank account
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        // Get the bank account
        var bankAccount = accountRepository.findById(event.getId());

        if (bankAccount.isEmpty()) {
            throw new IllegalArgumentException("Bank account not found");
        }
        // Get the current balance
        var currentBalance = bankAccount.get().getBalance();

        if (currentBalance - event.getAmount() < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Update the balance
        bankAccount.get().setBalance(currentBalance - event.getAmount());
        // Save the bank account
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(AccountClosedEvent event) {
        // Get the bank account
        var bankAccount = accountRepository.findById(event.getId());

        if (bankAccount.isEmpty()) {
            throw new IllegalArgumentException("Bank account not found");
        }
        // Delete the bank account
        accountRepository.delete(bankAccount.get());
    }
}
