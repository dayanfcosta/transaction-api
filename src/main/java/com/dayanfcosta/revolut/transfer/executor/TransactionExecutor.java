package com.dayanfcosta.revolut.transfer.executor;

import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;

/**
 * @author dayan.costa
 */
public interface TransactionExecutor {

  void execute(Transaction transaction);

  boolean apply(Transaction transaction);

}
