package com.dayanfcosta.revolut.transfer.model.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author dayan.costa
 */
@Schema(description = "Type of transaction")
public enum TransactionType {

  DEBIT,
  CREDIT,
  TRANSFER

}
