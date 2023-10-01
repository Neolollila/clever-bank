package com.devmandrik.service;

import com.devmandrik.dao.AccountDao;
import com.devmandrik.entity.Account;
import com.devmandrik.entity.Bank;
import com.devmandrik.entity.Transaction;
import com.devmandrik.entity.User;
import com.devmandrik.entity.enums.CurrencyType;
import com.devmandrik.entity.enums.TransactionType;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static com.devmandrik.entity.enums.TransactionType.*;

@NoArgsConstructor
public class ReceiptService {
    private final AccountService accountService = AccountService.getInstance();
    private final TransactionService transactionService = new TransactionService();

    public void transferReceipt(Long fromAccountId, Long toAccountId, Float sum) {
        Account fromAccount = accountService.findById(fromAccountId).get();
        Account toAccount = accountService.findById(toAccountId).get();
        Transaction transaction = createTransaction(TRANSFER, fromAccount.getCurrency(), sum,
                fromAccount.getBank(), fromAccount.getUser());
        transaction.setToBank(toAccount.getBank());
        transactionService.save(transaction);
        accountService.transfer(sum, fromAccount, toAccount);
        showTransferReceipt(transaction, fromAccount, toAccount);

    }

    public void depositReceipt(Long accountId,Float sum) {
        Account account = accountService.findById(accountId).get();
        Transaction transaction = createTransaction(DEPOSIT, account.getCurrency(), sum,
                account.getBank(), account.getUser());
        transactionService.save(transaction);
        accountService.deposit(sum, account);
        showDepositWithdrawReceipt(transaction, account);
    }

    public void withdrawReceipt(Long accountId,Float sum) {
        Account account = accountService.findById(accountId).get();
        Transaction transaction = createTransaction(WITHDRAW, account.getCurrency(), sum,
                account.getBank(), account.getUser());
        transactionService.save(transaction);
        accountService.withdraw(sum, account);
        showDepositWithdrawReceipt(transaction, account);
    }

    @SneakyThrows
    private void showTransferReceipt(Transaction transaction, Account fromAccount, Account toAccount) {
        String transferReceipt = String.format("""
                        -------------------------------
                        |        Банковский чек       |
                        |Чек:          %s             |
                        |%s      |
                        |Тип транзакции:  %s    |
                        |Банк отправителя: %s       |
                        |Банк получателя: %s        |
                        |Счёт отправителя: %s |
                        |Счёт получателя: %s   |
                        |Сумма:          %s %s|
                        |-----------------------------|
                        """, transaction.getId(), transaction.getCreatedAt(),
                transaction.getTransactionType(), transaction.getFromBank().getName(), transaction.getToBank().getName(),
                fromAccount.getId(), toAccount.getId(), transaction.getSum(), transaction.getCurrency());
        Path file = Path.of("check/check_"+fromAccount.getId()+"_"+LocalDate.now() +"_"+transaction.getTransactionType()+".txt");
        Files.write(file, transferReceipt.getBytes());
        System.out.println(transferReceipt);
    }

    @SneakyThrows
    private void showDepositWithdrawReceipt(Transaction transaction, Account fromAccount) {
        String depositWithdrawReceipt = String.format("""
                        -------------------------------
                        |        Банковский чек       |
                        |Чек:          %s             |
                        |%s      |
                        |Тип транзакции:  %s    |
                        |Банк: %s       |
                        |Счёт: %s |
                        |Сумма:          %s %s|
                        |-----------------------------|
                        """, transaction.getId(), transaction.getCreatedAt(),
                transaction.getTransactionType(), transaction.getFromBank().getName(),
                fromAccount.getId(), transaction.getSum(), transaction.getCurrency());
        Path file = Path.of("check/check_"+fromAccount.getId()+"_"+LocalDate.now() +"_"+transaction.getTransactionType()+".txt");
        Files.write(file, depositWithdrawReceipt.getBytes());
        System.out.println(depositWithdrawReceipt);
    }

    private Transaction createTransaction(TransactionType transactionType, CurrencyType currencyType, Float sum, Bank bank, User user) {
        return Transaction.builder()
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .transactionType(transactionType)
                .currency(currencyType)
                .sum(sum)
                .fromBank(bank)
                .toBank(bank)
                .user(user)
                .build();
    }
}
