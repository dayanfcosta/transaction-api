package com.dayanfcosta.revolut.transfer.model.account;

import com.dayanfcosta.revolut.transfer.command.account.AccountSaveCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.math.BigDecimal;

/**
 * @author dayan.costa
 */
@Schema(description = "Represents a system account")
public class Account {

  private long id;
  private String number;
  private BigDecimal balance = BigDecimal.ZERO;

  private Account() {
    super();
  }

  private Account(long id, String number) {
    this(number);
    this.id = id;
  }

  private Account(long id, String number, BigDecimal balance){
    this(id, number);
    this.balance = balance;
  }

  public Account(String number) {
    this.number = Validate.notBlank(number, "Account Number Invalid");
  }

  @Schema(description = "ID of an account", required = true)
  public long getId() {
    return id;
  }

  @Schema(description = "The number of the account", required = true)
  public String getNumber() {
    return number;
  }

  @Schema(description = "The balance of an account (initial value is zero)", required = true)
  public BigDecimal getBalance() {
    return balance;
  }

  public void debit(BigDecimal ammount) {
    Validate.notNull(ammount, "Invalid ammount");
    Validate.isTrue(ammount.compareTo(BigDecimal.ZERO) > 0, "Withdraws just allow ammounts bigger than 0 (zero)");
    Validate.isTrue(balance.subtract(ammount).compareTo(BigDecimal.ZERO) >= 0, "Insuficient funds");
    balance = balance.subtract(ammount);
  }

  public void credit(BigDecimal ammount) {
    Validate.notNull(ammount, "Invalid ammount");
    Validate.isTrue(ammount.compareTo(BigDecimal.ZERO) > 0, "Deposits just allow ammounts bigger than 0 (zero)");
    balance = balance.add(ammount);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account that = (Account) o;
    return new EqualsBuilder()
        .append(number, that.number)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return number.hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Account{");
    sb.append("number='").append(number).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public static Account from(AccountSaveCommand command) {
    return new Account(command.getNumber());
  }

  public static Account from(long id, String number) {
    return new Account(id, number);
  }

  public static Account from(Account account){
    return new Account(account.id, account.number, account.balance);
  }

}
