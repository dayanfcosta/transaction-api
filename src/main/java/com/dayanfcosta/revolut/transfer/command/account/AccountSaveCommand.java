package com.dayanfcosta.revolut.transfer.command.account;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author dayan.costa
 */
public class AccountSaveCommand {

  @NotBlank
  private String number;

  AccountSaveCommand() {
    super();
  }

  public AccountSaveCommand(String number) {
    this.number = number;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("AccountSaveCommand{");
    sb.append("number='").append(number).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
