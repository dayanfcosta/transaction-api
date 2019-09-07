package com.dayanfcosta.revolut.transfer.exception;

/**
 * @author dayan.costa
 */
public class AccountDuplicatedException extends RuntimeException {

  public AccountDuplicatedException() {
    super("Account already exists");
  }
}
