package com.dayanfcosta.revolut.transfer.exception;

/**
 * @author dayan.costa
 */
public class TransactionNotFoundException extends RuntimeException {

  public TransactionNotFoundException(long id) {
    super(String.format("Transaction %s not found", id));
  }
}
