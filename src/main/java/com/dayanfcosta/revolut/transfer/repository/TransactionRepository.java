package com.dayanfcosta.revolut.transfer.repository;

import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * @author dayan.costa
 */
public interface TransactionRepository {

  Transaction save(Transaction transaction);

  List<Transaction> findAll();

  void update(Transaction transaction);

  Optional<Transaction> findById(long id);

  List<Transaction> findAllByAccount(Account account);

}
