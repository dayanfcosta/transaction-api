package com.dayanfcosta.revolut.transfer.api.handler;

import com.dayanfcosta.revolut.transfer.exception.AccountNotFoundException;
import com.dayanfcosta.revolut.transfer.exception.TransactionNotFoundException;
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
@Requires(classes = {TransactionNotFoundException.class, ExceptionHandler.class})
public class TransactionNotFoundExceptionHandler implements ExceptionHandler<TransactionNotFoundException, HttpResponse> {

  @Override
  public HttpResponse handle(HttpRequest request, TransactionNotFoundException exception) {
    var error = new Error(HttpStatus.NOT_FOUND.getCode(), exception.getMessage());
    return HttpResponse.notFound(error);
  }

}
