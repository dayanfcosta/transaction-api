package com.dayanfcosta.revolut.transfer.api.dto;

import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionState;
import com.dayanfcosta.revolut.transfer.model.transaction.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dayan.costa
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TransactionDto {

  private final long id;
  private final long sourceId;
  private Long destinationId;
  private final BigDecimal ammount;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
  private final LocalDateTime date;
  private final TransactionType type;
  private final TransactionState state;


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
