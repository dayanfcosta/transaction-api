package com.dayanfcosta.revolut.transfer.command.account;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

/**
 * @author dayan.costa
 */
public class AccountSaveCommand {

  @NotBlank(message = "Account number is missing")
  private String number;

  AccountSaveCommand() {
    super();
  }

  public AccountSaveCommand(@NotBlank(message = "Account number is missing") String number) {
    this.number = number;
  }

  @Schema(description = "The number of the account to be created")
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
