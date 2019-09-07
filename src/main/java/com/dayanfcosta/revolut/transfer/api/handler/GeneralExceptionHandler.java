package com.dayanfcosta.revolut.transfer.api.handler;

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
@Requires(classes = {RuntimeException.class, ExceptionHandler.class})
public class GeneralExceptionHandler implements ExceptionHandler<RuntimeException, HttpResponse> {

  @Override
  public HttpResponse handle(HttpRequest request, RuntimeException exception) {
    return HttpResponse.serverError(exception.getMessage());
  }

}
