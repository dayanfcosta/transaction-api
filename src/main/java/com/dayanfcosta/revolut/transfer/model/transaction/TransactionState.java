package com.dayanfcosta.revolut.transfer.model.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author dayan.costa
 */
@Schema(description = "State of transaction")
public enum  TransactionState {
  PENDING,
  PROCESSING,
  DONE,
  ERROR
}
