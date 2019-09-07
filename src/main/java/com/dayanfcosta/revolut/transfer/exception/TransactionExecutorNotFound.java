package com.dayanfcosta.revolut.transfer.exception;

import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;

/**
 * @author dayan.costa
 */
public class TransactionExecutorNotFound extends RuntimeException {

  public TransactionExecutorNotFound(Transaction transaction) {
    super(String.format("No such transaction executor for transaction type %s", transaction.getType().name()));
  }
}
