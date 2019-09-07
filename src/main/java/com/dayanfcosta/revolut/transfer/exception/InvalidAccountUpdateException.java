package com.dayanfcosta.revolut.transfer.exception;

/**
 * @author dayan.costa
 */
public class InvalidAccountUpdateException extends RuntimeException {

  public InvalidAccountUpdateException() {
    super("Unable to update account number");
  }
}
