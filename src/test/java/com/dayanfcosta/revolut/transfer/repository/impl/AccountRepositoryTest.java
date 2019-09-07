package com.dayanfcosta.revolut.transfer.repository.impl;

import com.dayanfcosta.revolut.transfer.Application;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;
import io.micronaut.context.ApplicationContext;
import io.micronaut.test.annotation.MicronautTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author dayan.costa
 */
@MicronautTest
class AccountRepositoryTest {

  private Account account;
  private AccountRepository repository;

  @BeforeEach
  void setUp() {
    var context = ApplicationContext.build(Application.class).start();
    repository = context.getBean(AccountRepository.class);
    account = new Account("1234");
  }

  @Test
  @DisplayName("Find account by ID")
  void testFindById() {
    Assertions.assertThat(repository.findById(1L)).isEmpty();

    repository.save(account);

    var found = repository.findById(1L);
    Assertions.assertThat(found).isNotEmpty();
  }

  @Test
  @DisplayName("Insert account")
  void testSave() {
    Assertions.assertThat(repository.findAll()).hasSize(0);

    account = repository.save(account);

    assertThat(account.getId()).isGreaterThan(0);
    Assertions.assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  @DisplayName("Find all registered accounts")
  void testFindAll() {
    Assertions.assertThat(repository.findAll()).hasSize(0);

    repository.save(account);
    repository.save(new Account("123"));

    Assertions.assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  @DisplayName("Update existing account")
  void testUpdate() {
    var initialBalance = account.getBalance();
    account = repository.save(account);

    account.credit(BigDecimal.ONE);
    repository.update(account);

    var newAccount = repository.findById(account.getId());

    Assertions.assertThat(newAccount).isNotEmpty();
    Assertions.assertThat(newAccount.get().getBalance()).isEqualTo(initialBalance.add(BigDecimal.ONE));
  }

  @Test
  @DisplayName("Find account by number")
  void testFindByNumber() {
    var accountFound = repository.findByNumber("1234");
    Assertions.assertThat(accountFound).isEmpty();

    account = repository.save(account);
    accountFound = repository.findByNumber("1234");

    Assertions.assertThat(accountFound).isNotEmpty();
    Assertions.assertThat(accountFound.get()).isEqualTo(account);
  }

}