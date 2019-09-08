package com.dayanfcosta.revolut.transfer.command.transaction;

import com.dayanfcosta.revolut.transfer.model.transaction.TransactionType;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * @author dayan.costa
 */
@Schema(name = "TransactionSaveCommand", description = "The command to create a transaction")
public final class TransactionSaveCommand {

  @Schema(description = "The id of the source account of the transaction", required = true)
  @Positive(message = "Source account ID must be greater than xero")
  @NotNull(message = "Transaction must have a source account")
  private long sourceId;
  @Schema(description = "The id of the destination account of the transaction (only used for transfers)")
  private long destinationId;
  @Schema(description = "The ammount of the transaction", required = true)
  @Positive(message = "Transaction ammount must be greater than zero")
  private BigDecimal ammount;
  @Schema(required = true)
  @NotNull(message = "Transaction must have a type")
  private TransactionType type;

  TransactionSaveCommand() {
    super();
  }

  public TransactionSaveCommand(long sourceId, long destinationId, BigDecimal ammount, TransactionType type) {
    this.destinationId = destinationId;
    this.sourceId = sourceId;
    this.ammount = ammount;
    this.type = type;
  }

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
