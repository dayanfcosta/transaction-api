package com.dayanfcosta.revolut.transfer.model.transaction;

import com.dayanfcosta.revolut.transfer.model.account.Account;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public final class TransactionBuilder {

  private long id;
  private Account source;
  private BigDecimal ammount;
  private String errorMessage;
  private TransactionType type;
  private LocalDateTime date = LocalDateTime.now();
  private Optional<Account> destination = Optional.empty();
  private TransactionState state = TransactionState.PENDING;

  private TransactionBuilder() {
    super();
  }

  public static TransactionBuilder create() {
    return new TransactionBuilder();
  }

  public TransactionBuilder id(long id) {
    this.id = id;
    return this;
  }

  public TransactionBuilder type(TransactionType type) {
    this.type = Validate.notNull(type, "Invalid transaction type");
    return this;
  }

  public TransactionBuilder source(Account source) {
    this.source = Validate.notNull(source, "Source Account Invalid");
    return this;
  }

  public TransactionBuilder destination(Optional<Account> destination) {
    this.destination = destination;
    return this;
  }

  public TransactionBuilder ammount(BigDecimal ammount) {
    Validate.notNull(ammount, "Invalid ammount");
    Validate.isTrue(ammount.compareTo(BigDecimal.ZERO) > 0, "Transaction's ammout must be bigger than 0 (zero)");
    this.ammount = ammount;
    return this;
  }

  public TransactionBuilder state(TransactionState state) {
    this.state = Validate.notNull(state, "Invalid transaction state");
    ;
    return this;
  }

  public TransactionBuilder date(LocalDateTime date) {
    this.date = Validate.notNull(date, "Invalid transaction date");
    return this;
  }

  public TransactionBuilder errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

  public Transaction build() {
    if (TransactionType.TRANSFER.equals(type)) {
      Validate.isTrue(destination.isPresent(), "Transfers must have a destination account");
      Validate.isTrue(!source.equals(destination.get()), "Transfers must have differents source and destination accounts");
    }

    return new Transaction(id, source, destination, ammount, date, state, type, errorMessage);
  }

  public Transaction build(Transaction transaction) {
    errorMessage(transaction.getErrorMessage());
    destination(transaction.getDestination());
    ammount(transaction.getAmmount());
    source(transaction.getSource());
    state(transaction.getState());
    type(transaction.getType());
    date(transaction.getDate());
    id(transaction.getId());
    return build();
  }

  public Transaction build(Transaction transaction, boolean newInstance) {
    if (!newInstance) {
      id = transaction.getId();
    }

    destination(transaction.getDestination());
    ammount(transaction.getAmmount());
    source(transaction.getSource());
    state(transaction.getState());
    type(transaction.getType());
    date(transaction.getDate());
    id(id);

    return build();
  }
}