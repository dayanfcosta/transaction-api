package com.dayanfcosta.revolut.transfer.api.handler;

import com.dayanfcosta.revolut.transfer.exception.AccountNotFoundException;
import com.dayanfcosta.revolut.transfer.exception.TransactionNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
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
    return HttpResponse.notFound(exception.getMessage());
  }

}
