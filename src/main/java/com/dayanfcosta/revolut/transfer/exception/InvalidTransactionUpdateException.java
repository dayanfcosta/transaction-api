package com.dayanfcosta.revolut.transfer.exception;

/**
 * @author dayan.costa
 */
public class InvalidTransactionUpdateException extends RuntimeException {

  public InvalidTransactionUpdateException() {
    super("Unable to update a transaction field other than state");
  }
}
