package com.dayanfcosta.revolut.transfer.exception;

import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;

/**
 * @author dayan.costa
 */
public class InvalidTransactionExecutorException extends RuntimeException {

  public InvalidTransactionExecutorException(Transaction transaction) {
    super(String.format("Invalid executor for transaction %s", transaction));
  }
}
