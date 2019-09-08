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

  private long id;
  private long sourceId;
  private Long destinationId;
  private BigDecimal ammount;
  @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
  private LocalDateTime date;
  private TransactionType type;
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
