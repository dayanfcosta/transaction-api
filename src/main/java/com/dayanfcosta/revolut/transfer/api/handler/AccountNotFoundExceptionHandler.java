package com.dayanfcosta.revolut.transfer.api.handler;

import com.dayanfcosta.revolut.transfer.exception.AccountNotFoundException;
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
@Requires(classes = {AccountNotFoundException.class, ExceptionHandler.class})
public class AccountNotFoundExceptionHandler implements ExceptionHandler<AccountNotFoundException, HttpResponse> {

  @Override
  public HttpResponse handle(HttpRequest request, AccountNotFoundException exception) {
    var error = new Error(HttpStatus.NOT_FOUND.getCode(), exception.getMessage());
    return HttpResponse.notFound(error);
  }

}
