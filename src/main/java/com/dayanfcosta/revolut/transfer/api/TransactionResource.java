package com.dayanfcosta.revolut.transfer.api;

import com.dayanfcosta.revolut.transfer.api.handler.Error;
import com.dayanfcosta.revolut.transfer.command.transaction.TransactionSaveCommand;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.model.transaction.Transaction;
import com.dayanfcosta.revolut.transfer.api.dto.TransactionDto;
import com.dayanfcosta.revolut.transfer.service.TransactionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

/**
 * @author dayan.costa
 */
@Validated
@Tag(name = "Transactions")
@Controller("/api/transactions")
public class TransactionResource {

  private final TransactionService service;

  public TransactionResource(TransactionService service) {
    this.service = service;
  }

  @Operation(
      summary = "Creates a new transaction",
      description = "A new transaction with the given data is created"
  )
  @ApiResponse(responseCode = "201",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = TransactionDto.class)
      ),
      description = "Returns the created transaction"
  )
  @ApiResponse(responseCode = "409",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Error.class)
      ),
      description = "A transaction with the given data was already created at the same time"
  )
  @ApiResponse(responseCode = "500",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Error.class)
      ),
      description = "An error occurred while creating the transaction"
  )
  @Post
  public Single<HttpResponse<TransactionDto>> create(@Valid @Body Single<TransactionSaveCommand> command) {
    return command.map(c -> {
      var transaction = service.create(c);
      return HttpResponse
          .created(new TransactionDto(transaction))
          .headers(headers -> headers.location(location(transaction)));
    });
  }

  @Operation(
      summary = "Find a transaction by its id",
      description = "Find an existing transaction"
  )
  @ApiResponse(
      responseCode = "200",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = TransactionDto.class)
      ),
      description = "Return found transaction"
  )
  @ApiResponse(responseCode = "404",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Error.class)
      ),
      description = "No transaction with the given ID was found"
  )
  @ApiResponse(responseCode = "500",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Error.class)
      ),
      description = "An error occurred while searching the transaction"
  )
  @Get("/{id}")
  public Single<TransactionDto> byId(@NotNull long id) {
    return Single.just(service.findById(id));
  }

  @Operation(summary = "List all transactions of a given account id")
  @ApiResponse(
      responseCode = "200",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          array = @ArraySchema(schema = @Schema(implementation = TransactionDto.class))
      ),
      description = "Return found transactions of the given account"
  )
  @ApiResponse(responseCode = "500",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Error.class)
      ),
      description = "An error occurred while listing transactions"
  )
  @Get("/account/{id}")
  public Single<List<TransactionDto>> fromAccount(@NotNull long id) {
    return Single.just(service.fromAccount(id));
  }

  private URI location(Transaction transaction) {
    return URI.create(String.format("/api/transactions/%s", transaction.getId()));
  }
}
