package com.dayanfcosta.revolut.transfer.api.dto;

import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionState;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dayan.costa
 */
@Schema(description = "Represents the basics of a transaction")
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TransactionDto {

  @Schema(description = "Transaction's id", required = true)
  private long id;
  @Schema(description = "The source of the transaction", required = true)
  private long sourceId;
  @Schema(description = "The final destination of the transaction (only used for transfers)")
  private Long destinationId;
  @Schema(description = "The ammount of the transaction", required = true)
  private BigDecimal ammount;
  @Schema(description = "The date of when transaction was created", required = true)
  @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
  private LocalDateTime date;
  @Schema(description = "Transaction's Type", allowableValues = {"CREDIT", "DEBIT", "TRANSFER"}, required = true)
  private TransactionType type;
  @Schema(description = "Transaction's state", allowableValues = {"DONE", "PROCCESSING", "PENDING", "ERROR"},
      required = true)
  private TransactionState state;

  public TransactionDto() {
  }

  public TransactionDto(@NotNull Transaction transaction) {
    id = transaction.getId();
    date = transaction.getDate();
    type = transaction.getType();
    state = transaction.getState();
    ammount = transaction.getAmmount();
    sourceId = transaction.getSource().getId();

    transaction.getDestination()
        .ifPresentOrElse(account -> destinationId = account.getId(), () -> destinationId = null);
  }

  public long getId() {
    return id;
  }

  public long getSourceId() {
    return sourceId;
  }

  public Long getDestinationId() {
    return destinationId;
  }

  public BigDecimal getAmmount() {
    return ammount;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public TransactionType getType() {
    return type;
  }

  public TransactionState getState() {
    return state;
  }
}
