package com.dayanfcosta.revolut.transfer.service;

import com.dayanfcosta.revolut.transfer.Application;
import com.dayanfcosta.revolut.transfer.command.account.AccountSaveCommand;
import com.dayanfcosta.revolut.transfer.exception.AccountNotFoundException;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * @author dayan.costa
 */
class AccountServiceTest {

  private AccountService service;

  @BeforeEach
  void setUp() {
    var context = ApplicationContext.build(Application.class).start();
    service = context.createBean(AccountService.class);
  }

  @Test
  @DisplayName("Should create an account given a Command")
  void testCreateAccount() {
    var command = new AccountSaveCommand("1");

    var account = service.create(command);

    assertThat(account).isNotNull();
    assertThat(account.getId()).isGreaterThan(0);
  }

  @Test
  @DisplayName("Should throw an NPE because the command is invalid")
  void testCreateAccount_nullCommand() {
    assertThatNullPointerException()
        .isThrownBy(() -> service.create(null))
        .withMessage("Invalid account data");
  }

  @Test
  @DisplayName("Should throw an NPE because the account is not found")
  void testFindById_notFound() {
    assertThatThrownBy(() -> service.findById(1L))
        .isInstanceOf(AccountNotFoundException.class)
        .hasMessage("Account does not exist");
  }

  @Test
  @DisplayName("Should find an account with the given id")
  void testFindById() {
    var account = service.create(new AccountSaveCommand("1"));

    var found = service.findById(account.getId());

    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(account.getId());
  }

  @Test
  @DisplayName("Should return all accounts save")
  void testFindAll() {
    assertThat(service.findAll()).hasSize(0);

    service.create(new AccountSaveCommand("1"));

    assertThat(service.findAll()).hasSize(1);
  }

}