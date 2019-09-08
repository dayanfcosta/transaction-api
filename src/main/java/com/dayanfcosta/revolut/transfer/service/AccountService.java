package com.dayanfcosta.revolut.transfer.service;

import com.dayanfcosta.revolut.transfer.command.account.AccountSaveCommand;
import com.dayanfcosta.revolut.transfer.exception.AccountNotFoundException;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import java.util.List;

/**
 * @author dayan.costa
 */
@Singleton
public class AccountService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

  @Inject
  private AccountRepository accountRepository;

  public Account create(@Valid AccountSaveCommand command) {
    LOGGER.debug("Inserting account {}", command);
    Validate.notNull(command, "Invalid account data");
    var created = accountRepository.save(Account.from(command));
    LOGGER.info("Account created: {}", created);
    return created;
  }

  public List<Account> findAll() {
    return accountRepository.findAll();
  }

  public Account findById(long id) {
    return accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
  }

}
