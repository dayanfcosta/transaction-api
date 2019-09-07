package com.dayanfcosta.revolut.transfer.executor;

import com.dayanfcosta.revolut.transfer.Application;
import com.dayanfcosta.revolut.transfer.exception.InvalidTransactionExecutorException;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionBuilder;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;
import com.dayanfcosta.revolut.transfer.repository.TransactionRepository;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionType;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;

/**
 * @author dayan.costa
 */
class TransferExecutorTest {

  private TransactionExecutor executor;
  private AccountRepository accountRepository;
  private TransactionRepository transactionRepository;

  private Account source;
  private Account destination;
  private Transaction transfer;

  @BeforeEach
  void setUp() {
    var context = ApplicationContext.build(Application.class).start();
    transactionRepository = context.getBean(TransactionRepository.class);
    accountRepository = context.getBean(AccountRepository.class);
    executor = new TransferExecutor(accountRepository, transactionRepository);

    source = accountRepository.save(new Account("1"));
    destination = accountRepository.save(new Account("2"));

    transfer = TransactionBuilder.create()
        .type(TransactionType.TRANSFER)
        .source(source)
        .destination(of(destination))
        .ammount(TEN)
        .build();
  }

  @Test
  @DisplayName("Should throw an exception when passing invalid transaction type")
  void testExecute_invalidTransactionType() {
    var credit = TransactionBuilder.create().ammount(ONE).source(source).type(TransactionType.CREDIT).build();

    assertThatThrownBy(() -> executor.execute(credit))
        .isInstanceOf(InvalidTransactionExecutorException.class);
  }

  @Test
  @DisplayName("Transaction should be executed successfully")
  void testExecute_successfulTransaction() {
    initBalances();
    var sourceBalance = source.getBalance();
    var destinationBalance = destination.getBalance();

    transfer = transactionRepository.save(transfer);

    executor.execute(transfer);

    source = accountRepository.findById(source.getId()).get();
    destination = accountRepository.findById(destination.getId()).get();

    assertThat(source.getBalance()).isEqualTo(sourceBalance.subtract(transfer.getAmmount()));
    assertThat(destination.getBalance()).isEqualTo(destinationBalance.add(transfer.getAmmount()));
  }

  @Test
  void testApply() {
    var credit = TransactionBuilder.create().ammount(ONE).source(source).type(TransactionType.CREDIT).build();

    assertThat(executor.apply(transfer)).isTrue();
    assertThat(executor.apply(credit)).isFalse();
  }

  private void initBalances() {
    source.credit(BigDecimal.valueOf(100));
    destination.credit(BigDecimal.valueOf(100));

    accountRepository.update(source);
    accountRepository.update(destination);
  }

}