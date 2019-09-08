package com.dayanfcosta.revolut.transfer.service;

import com.dayanfcosta.revolut.transfer.Application;
import com.dayanfcosta.revolut.transfer.command.transaction.TransactionSaveCommand;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;
import com.dayanfcosta.revolut.transfer.repository.TransactionRepository;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.dayanfcosta.revolut.transfer.model.transaction.TransactionState.DONE;
import static com.dayanfcosta.revolut.transfer.model.transaction.TransactionState.ERROR;
import static com.dayanfcosta.revolut.transfer.model.transaction.TransactionType.*;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author dayan.costa
 */
class TransactionServiceTest {

  private Account source;
  private Account destination;

  private AccountRepository accountRepository;
  private TransactionService transactionService;
  private TransactionRepository transactionRepository;

  @BeforeEach
  void setUp() {
    var context = ApplicationContext.build(Application.class).start();
    transactionRepository = context.getBean(TransactionRepository.class);
    transactionService = context.getBean(TransactionService.class);
    accountRepository = context.getBean(AccountRepository.class);

    source = accountRepository.save(new Account("1"));
    destination = accountRepository.save(new Account("2"));
  }

  @Test
  @DisplayName("Should create a credit transaction finished without errors")
  void testCreate_creditTransaction() {
    // given
    var command = new TransactionSaveCommand(source.getId(), 0, TEN, CREDIT);

    // when
    var transaction = transactionService.create(command);

    // then
    transaction = transactionRepository.findById(transaction.getId()).get();
    assertThat(transaction.getState()).isEqualTo(DONE);
    assertThat(transaction.getType()).isEqualTo(CREDIT);
  }

  @Test
  @DisplayName("Should create a debit transaction finished without errors")
  void testCreate_debitTransaction() {
    // given
    var command = new TransactionSaveCommand(source.getId(), 0, TEN, CREDIT);
    transactionService.create(command);
    command = new TransactionSaveCommand(source.getId(), 0, ONE, DEBIT);

    // when
    var transaction = transactionService.create(command);

    // then
    transaction = transactionRepository.findById(transaction.getId()).get();
    source = accountRepository.findById(source.getId()).get();

    assertThat(transaction.getState()).isEqualTo(DONE);
    assertThat(transaction.getType()).isEqualTo(DEBIT);
    assertThat(source.getBalance()).isEqualTo(BigDecimal.valueOf(9));
  }

  @Test
  @DisplayName("Should create a debit transaction with error of insuficient funds")
  void testCreate_debitTransaction_insuficientFunds() {
    // given
    var initialBalance = source.getBalance();
    var command = new TransactionSaveCommand(source.getId(), 0, ONE, DEBIT);

    // when
    var transaction = transactionService.create(command);


    // then
    transaction = transactionRepository.findById(transaction.getId()).get();
    source = accountRepository.findById(source.getId()).get();

    assertThat(transaction.getType()).isEqualTo(DEBIT);
    assertThat(transaction.getState()).isEqualTo(ERROR);
    assertThat(source.getBalance()).isEqualTo(initialBalance);
    assertThat(transaction.getErrorMessage()).isEqualTo("Insuficient funds");
  }

  @Test
  @DisplayName("Should create a transfer transaction finished without errors")
  void testCreate_transferTransaction() {
    //given
    source.credit(TEN);
    accountRepository.update(source);
    var initialSourceBalance = source.getBalance();
    var initialDestBalance = destination.getBalance();
    var command = new TransactionSaveCommand(source.getId(), destination.getId(), ONE, TRANSFER);

    // when
    var transaction = transactionService.create(command);

    // then
    transaction = transactionRepository.findById(transaction.getId()).get();
    destination = accountRepository.findById(destination.getId()).get();
    source = accountRepository.findById(source.getId()).get();

    assertThat(transaction.getState()).isEqualTo(DONE);
    assertThat(source.getBalance()).isEqualTo(initialSourceBalance.subtract(transaction.getAmmount()));
    assertThat(destination.getBalance()).isEqualTo(initialDestBalance.add(transaction.getAmmount()));
  }

  @Test
  @DisplayName("Should create a transfer transaction with insuficient funds in source account")
  void testCreate_transferTransaction_insuficientFunds() {
    // given
    var initialSourceBalance = source.getBalance();
    var initialDestBalance = destination.getBalance();
    var command = new TransactionSaveCommand(source.getId(), destination.getId(), ONE, TRANSFER);

    // when
    var transaction = transactionService.create(command);

    // then
    transaction = transactionRepository.findById(transaction.getId()).get();
    destination = accountRepository.findById(destination.getId()).get();
    source = accountRepository.findById(source.getId()).get();

    assertThat(transaction.getState()).isEqualTo(ERROR);
    assertThat(source.getBalance()).isEqualTo(initialSourceBalance);
    assertThat(destination.getBalance()).isEqualTo(initialDestBalance);
    assertThat(transaction.getErrorMessage()).isEqualTo("Insuficient funds");
  }

  @Test
  @DisplayName("Should show all transactions from a given account")
  void testFromAccount() {
    //given
    source.credit(TEN);
    accountRepository.update(source);
    var command = new TransactionSaveCommand(source.getId(), destination.getId(), ONE, TRANSFER);

    // when
    transactionService.create(command);

    // then
    assertThat(transactionService.fromAccount(source.getId())).hasSize(1);
    assertThat(transactionService.fromAccount(destination.getId())).hasSize(1);
  }

}