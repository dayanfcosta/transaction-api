package com.dayanfcosta.revolut.transfer.repository.impl;

import com.dayanfcosta.revolut.transfer.exception.AccountDuplicatedException;
import com.dayanfcosta.revolut.transfer.exception.InvalidAccountUpdateException;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.repository.AccountRepository;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author dayan.costa
 */
@Singleton
class AccountRepositoryImpl implements AccountRepository {

  private final AtomicLong currentId;
  private final Map<Long, Account> accounts;

  public AccountRepositoryImpl() {
    this.currentId = new AtomicLong(0L);
    this.accounts = new ConcurrentHashMap<>();
  }

  @Override
  public Optional<Account> findById(long id) {
    return Optional.ofNullable(this.accounts.get(id));
  }

  @Override
  public Account save(Account account) {
    validateDuplicated(account);
    var id = currentId.incrementAndGet();
    final var newAccount = Account.from(id, account.getNumber());
    accounts.putIfAbsent(id, newAccount);
    return newAccount;
  }

  @Override
  public List<Account> findAll() {
    return accounts.values()
        .stream()
        .map(Account::from)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Account> findByNumber(String number) {
    var account = new Account(number);
    return accounts.values()
        .stream()
        .filter(a -> a.equals(account))
        .map(Account::from)
        .findFirst();
  }

  @Override
  public void update(Account account) {
    validateUpdate(account);
    accounts.replace(account.getId(), account);
  }

  private void validateDuplicated(Account account) {
    Predicate<Account> predicate = a -> a.equals(account);
    if (accounts.values().stream().anyMatch(predicate)) {
      throw new AccountDuplicatedException();
    }
  }

  private void validateUpdate(Account account) {
    var existent = accounts.get(account.getId());
    if (!existent.equals(account)) {
      throw new InvalidAccountUpdateException();
    }
  }

}
