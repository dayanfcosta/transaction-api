package com.dayanfcosta.revolut.transfer.exception;

/**
 * @author dayan.costa
 */
public class TransactionDuplicatedException extends RuntimeException {

  public TransactionDuplicatedException() {
    super("Transaction duplicated");
  }
}
