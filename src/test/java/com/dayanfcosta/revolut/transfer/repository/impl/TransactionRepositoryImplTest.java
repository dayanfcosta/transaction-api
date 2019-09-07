package com.dayanfcosta.revolut.transfer.repository.impl;

import com.dayanfcosta.revolut.transfer.Application;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionBuilder;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionState;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionType;
import com.dayanfcosta.revolut.transfer.repository.TransactionRepository;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.TEN;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;

/**
 * @author dayan.costa
 */
class TransactionRepositoryImplTest {

  private Transaction transaction;
  private TransactionRepository repository;

  @BeforeEach
  void setUp() {
    var context = ApplicationContext.build(Application.class).start();
    repository = context.getBean(TransactionRepository.class);

    transaction = TransactionBuilder.create()
        .ammount(TEN)
        .source(new Account("1"))
        .type(TransactionType.DEBIT)
        .build();
  }

  @Test
  @DisplayName("Should save a new transaction")
  void testSave() {
    assertThat(repository.findAll()).hasSize(0);

    repository.save(transaction);

    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  @DisplayName("Should uupdate an existing transaction")
  void testUpdate() {
    transaction = repository.save(transaction);

    transaction.initProcess();

    repository.update(transaction);

    var byId = repository.findById(transaction.getId());
    assertThat(byId.get().getState().equals(TransactionState.PROCESSING)).isTrue();
  }

  @Test
  @DisplayName("Should find an existing transaction by its id")
  void testFindById() {
    assertThat(repository.findAll()).hasSize(0);

    transaction = repository.save(transaction);

    var byId = repository.findById(transaction.getId());

    assertThat(byId).isNotEmpty();
    assertThat(byId.get()).isEqualTo(transaction);
  }

  @Test
  @DisplayName("Should return all transactions from an account")
  void testFindAllByAccount() {
    var transfer = TransactionBuilder.create()
        .destination(of(new Account("2")))
        .source(new Account("1"))
        .type(TransactionType.TRANSFER)
        .ammount(TEN)
        .build();

    repository.save(transaction);
    repository.save(transfer);

    assertThat(repository.findAllByAccount(transfer.getSource())).hasSize(2);
    assertThat(repository.findAllByAccount(transfer.getDestination().get())).hasSize(1);
  }
}