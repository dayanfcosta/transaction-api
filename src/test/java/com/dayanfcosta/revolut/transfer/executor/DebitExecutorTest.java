package com.dayanfcosta.revolut.transfer.executor;

import com.dayanfcosta.revolut.transfer.Application;
import com.dayanfcosta.revolut.transfer.exception.InvalidTransactionExecutorException;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionBuilder;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionType;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;
import com.dayanfcosta.revolut.transfer.repository.TransactionRepository;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionState;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * @author dayan.costa
 */
class DebitExecutorTest {

  private TransactionExecutor executor;
  private AccountRepository accountRepository;
  private TransactionRepository transactionRepository;

  private Account account;
  private Transaction debit;
  private Transaction credit;

  @BeforeEach
  void setUp() {
    var context = ApplicationContext.build(Application.class).start();
    transactionRepository = context.getBean(TransactionRepository.class);
    accountRepository = context.getBean(AccountRepository.class);
    executor = new DebitExecutor(accountRepository, transactionRepository);

    account = accountRepository.save(new Account("123"));
    debit = transactionRepository.save(get(TransactionType.DEBIT));
    credit = transactionRepository.save(get(TransactionType.CREDIT));
  }

  @Test
  @DisplayName("Should throw an exception when passing invalid transaction type")
  void testExecute_invalidTransactionType() {
    assertThatThrownBy(() -> executor.execute(credit))
        .isInstanceOf(InvalidTransactionExecutorException.class);
  }

  @Test
  @DisplayName("Transaction should be executed successfully")
  void testExecute_successfulTransaction() {
    account.credit(BigDecimal.TEN);
    accountRepository.update(account);

    executor.execute(debit);

    var executed = transactionRepository.findById(debit.getId());
    account = accountRepository.findById(account.getId()).get();

    assertThat(executed).isPresent();
    assertThat(executed.get().getState()).isEqualTo(TransactionState.DONE);
    assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(9));
  }

  @Test
  @DisplayName("Execute transaction with insuficient funds")
  void testExecute_insuficientFunds() {
    executor.execute(debit);

    var executed = transactionRepository.findById(1L);
    assertThat(executed).isPresent();
    assertThat(executed.get().getState()).isEqualTo(TransactionState.ERROR);
  }

  @Test
  @DisplayName("Should check if an transaction is appliable to this executor")
  void testApply() {
    assertThat(executor.apply(debit)).isTrue();
    assertThat(executor.apply(credit)).isFalse();
  }

  private Transaction get(TransactionType type) {
    return TransactionBuilder.create()
        .ammount(BigDecimal.ONE)
        .source(account)
        .type(type)
        .build();
  }
}