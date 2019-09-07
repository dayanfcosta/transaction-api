package com.dayanfcosta.revolut.transfer.exception;

/**
 * @author dayan.costa
 */
public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException() {
    super("Invalid account");
  }
}
