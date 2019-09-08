package com.dayanfcosta.revolut.transfer.api.handler;

import com.dayanfcosta.revolut.transfer.exception.AccountDuplicatedException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

/**
 * @author dayan.costa
 */
@Singleton
@Produces
@Requires(classes = {AccountDuplicatedException.class, ExceptionHandler.class})
public class AccountDuplicatedExceptionHandler implements ExceptionHandler<AccountDuplicatedException, HttpResponse> {

  @Override
  public HttpResponse handle(HttpRequest request, AccountDuplicatedException exception) {
    var error = new Error(HttpStatus.CONFLICT.getCode(), exception.getMessage());
    return HttpResponse
        .status(HttpStatus.CONFLICT)
        .body(error);
  }

}
