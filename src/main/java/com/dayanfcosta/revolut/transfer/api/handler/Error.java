package com.dayanfcosta.revolut.transfer.api.handler;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author dayan.costa
 */
@Schema(description = "Application's error schema")
public class Error {

  private final int status;
  private final String message;

  public Error(int status, String message) {
    this.message = message;
    this.status = status;
  }

  @Schema(description = "The error's HTTP status code")
  public int getStatus() {
    return status;
  }

  @Schema(description = "The error message")
  public String getMessage() {
    return message;
  }
}
