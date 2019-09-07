package com.dayanfcosta.revolut.transfer.command.transaction;

import com.dayanfcosta.revolut.transfer.model.transaction.TransactionType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * @author dayan.costa
 */
public final class TransactionSaveCommand {

  @Positive(message = "Source account ID must be greater than xero")
  @NotNull(message = "Transaction must have a source account")
  private long sourceId;
  private long destinationId;
  @Positive(message = "Transaction ammount must be greater than zero")
  private BigDecimal ammount;
  @NotNull(message = "Transaction must have a type")
  private TransactionType type;

  public long getSourceId() {
    return sourceId;
  }

  public long getDestinationId() {
    return destinationId;
  }

  public BigDecimal getAmmount() {
    return ammount;
  }

  public TransactionType getType() {
    return type;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("TransactionSaveCommand{");
    sb.append("sourceId=").append(sourceId);
    sb.append(", destinationId=").append(destinationId);
    sb.append(", ammount=").append(ammount);
    sb.append(", type=").append(type);
    sb.append('}');
    return sb.toString();
  }
}
