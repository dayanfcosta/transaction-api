package com.dayanfcosta.revolut.transfer.service;

import com.dayanfcosta.revolut.transfer.api.dto.TransactionDto;
import com.dayanfcosta.revolut.transfer.command.transaction.TransactionSaveCommand;
import com.dayanfcosta.revolut.transfer.exception.AccountNotFoundException;
import com.dayanfcosta.revolut.transfer.exception.TransactionNotFoundException;
import com.dayanfcosta.revolut.transfer.executor.ExecutorManager;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionBuilder;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;
import com.dayanfcosta.revolut.transfer.repository.TransactionRepository;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dayan.costa
 */
@Singleton
public class TransactionService {

  private final ExecutorManager executorManager;
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  public TransactionService(AccountRepository accountRepository,
                            TransactionRepository transactionRepository,
                            ExecutorManager executorManager) {
    this.executorManager = executorManager;
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  public Transaction create(TransactionSaveCommand command) {
    final var transaction = transactionRepository.save(transaction(command));
    executorManager.execute(transaction);
    return transaction;
  }

  public List<TransactionDto> fromAccount(long id) {
    var account = accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
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

  private Transaction transaction(final TransactionSaveCommand command) {
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
