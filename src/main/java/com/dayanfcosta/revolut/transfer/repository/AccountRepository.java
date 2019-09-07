package com.dayanfcosta.revolut.transfer.repository;

import com.dayanfcosta.revolut.transfer.model.account.Account;

import java.util.List;
import java.util.Optional;

/**
 * @author dayan.costa
 */
public interface AccountRepository {

  Optional<Account> findById(long id);

  Account save(Account account);

  List<Account> findAll();

  Optional<Account> findByNumber(String number);

  void update(Account account);
}