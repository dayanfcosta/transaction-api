package com.dayanfcosta.revolut.transfer.service;

import com.dayanfcosta.revolut.transfer.command.transaction.TransactionSaveCommand;
import com.dayanfcosta.revolut.transfer.exception.AccountNotFoundException;
import com.dayanfcosta.revolut.transfer.exception.TransactionExecutorNotFound;
import com.dayanfcosta.revolut.transfer.exception.TransactionNotFoundException;
import com.dayanfcosta.revolut.transfer.executor.TransactionExecutor;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionBuilder;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;
import com.dayanfcosta.revolut.transfer.repository.TransactionRepository;
import com.dayanfcosta.revolut.transfer.api.dto.TransactionDto;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dayan.costa
 */
@Singleton
public class TransactionService {

  private final List<TransactionExecutor> executors;
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository,
                            List<TransactionExecutor> executors) {
    this.executors = executors;
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  public Transaction create(TransactionSaveCommand command) {
    final var transaction = transactionRepository.save(transaction(command));
    executor(transaction).execute(transaction);
    return transaction;
  }

  public List<TransactionDto> fromAccount(String number) {
    var account = accountRepository.findByNumber(number).orElseThrow(AccountNotFoundException::new);
    return transactionRepository.findAllByAccount(account)
        .stream()
        .map(TransactionDto::new)
        .collect(Collectors.toList());
  }

  public TransactionDto findById(long id) {
    var transaction = transactionRepository.findById(id)
        .orElseThrow(() -> new TransactionNotFoundException(id));
    return new TransactionDto(transaction);
  }

  private TransactionExecutor executor(Transaction transaction) {
    return executors.stream().filter(executor -> executor.apply(transaction))
        .findFirst()
        .orElseThrow(() -> new TransactionExecutorNotFound(transaction));
  }

  private Transaction transaction(TransactionSaveCommand command) {
    var sourceAccount = account(command.getSourceId()).orElseThrow(AccountNotFoundException::new);
    return TransactionBuilder.create()
        .source(sourceAccount)
        .type(command.getType())
        .ammount(command.getAmmount())
        .destination(account(command.getDestinationId()))
        .build();
  }

  private Optional<Account> account(long id) {
    return accountRepository.findById(id);
  }
}
