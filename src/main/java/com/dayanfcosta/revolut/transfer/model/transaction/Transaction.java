package com.dayanfcosta.revolut.transfer.model.transaction;

import com.dayanfcosta.revolut.transfer.model.account.Account;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author dayan.costa
 */
public class Transaction {

  private long id;
  private final Account source;
  private TransactionState state;
  private final LocalDateTime date;
  private final String errorMessage;
  private final BigDecimal ammount;
  private final TransactionType type;
  private final Optional<Account> destination;

  Transaction(long id, Account source, Optional<Account> destination, BigDecimal ammount,
              LocalDateTime date, TransactionState state, TransactionType type, String errorMessage) {
    this.errorMessage = errorMessage;
    this.destination = destination;
    this.ammount = ammount;
    this.source = source;
    this.state = state;
    this.date = date;
    this.type = type;
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public Account getSource() {
    return source;
  }

  public BigDecimal getAmmount() {
    return ammount;
  }

  public Optional<Account> getDestination() {
    return destination;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public TransactionState getState() {
    return state;
  }

  public TransactionType getType() {
    return type;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public boolean isTransfer() {
    return TransactionType.TRANSFER.equals(type);
  }

  public boolean contains(Account account) {
    return (destination.isPresent() && destination.get().equals(account)) || account.equals(source);
  }

  public void initProcess() {
    state = TransactionState.PROCESSING;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Transaction that = (Transaction) o;
    return new EqualsBuilder()
        .append(source, that.source)
        .append(ammount, that.ammount)
        .append(destination, that.destination)
        .append(type, that.type)
        .append(date, that.date)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(source)
        .append(ammount)
        .append(destination)
        .append(type)
        .append(date)
        .toHashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Transaction{");
    sb.append("source=").append(source);
    sb.append(", ammount=").append(ammount);
    sb.append(", destination=").append(destination);
    sb.append(", date=").append(date);
    sb.append('}');
    return sb.toString();
  }
}
