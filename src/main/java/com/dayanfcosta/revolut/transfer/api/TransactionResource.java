package com.dayanfcosta.revolut.transfer.api;

import com.dayanfcosta.revolut.transfer.command.transaction.TransactionSaveCommand;
import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.api.dto.TransactionDto;
import com.dayanfcosta.revolut.transfer.service.TransactionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Single;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

/**
 * @author dayan.costa
 */
@Validated
@Controller("/api/transactions")
public class TransactionResource {

  private final TransactionService service;

  public TransactionResource(TransactionService service) {
    this.service = service;
  }

  @Post
  public Single<HttpResponse<TransactionDto>> create(@Valid @Body Single<TransactionSaveCommand> command) {
    return command.map(c -> {
      var transaction = service.create(c);
      return HttpResponse
          .created(new TransactionDto(transaction))
          .headers(headers -> headers.location(location(transaction)));
    });
  }

  @Get("/{id}")
  public Single<TransactionDto> byId(@NotNull long id) {
    return Single.just(service.findById(id));
  }

  @Get("/account/{id}")
  public Single<List<TransactionDto>> fromAccount(@NotNull long id) {
    return Single.just(service.fromAccount(id));
  }

  private URI location(Transaction transaction) {
    return URI.create(String.format("/api/transactions/%s", transaction.getId()));
  }
}
