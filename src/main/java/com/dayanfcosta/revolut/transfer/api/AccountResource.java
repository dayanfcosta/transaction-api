package com.dayanfcosta.revolut.transfer.api;

import com.dayanfcosta.revolut.transfer.api.handler.Error;
import com.dayanfcosta.revolut.transfer.command.account.AccountSaveCommand;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.service.AccountService;
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
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

/**
 * @author dayan.costa
 */
@Validated
@Tag(name = "Accounts")
@Controller("/api/accounts")
public class AccountResource {

  private final AccountService service;

  public AccountResource(AccountService service) {
    this.service = service;
  }


  @Operation(summary = "List all registered accounts")
  @ApiResponse(
      responseCode = "200",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          array = @ArraySchema(schema = @Schema(implementation = Account.class))
      ),
      description = "Return found accounts"
  )
  @ApiResponse(responseCode = "500",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Error.class)
      ),
      description = "An error occurred while listing accounts"
  )
  @Get(produces = MediaType.APPLICATION_JSON)
  public Single<List<Account>> findAll() {
    return Single.just(service.findAll());
  }

  @Operation(
      summary = "Find an account by its id",
      description = "Find an existing account"
  )
  @ApiResponse(
      responseCode = "200",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Account.class)
      ),
      description = "Return found account"
  )
  @ApiResponse(responseCode = "404",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Error.class)
      ),
      description = "No account with given ID was found"
  )
  @ApiResponse(responseCode = "500",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Error.class)
      ),
      description = "An error occurred while searching the account"
  )
  @Get("/{id}")
  public Single<Account> byId(@NotNull long id) {
    return Single.just(service.findById(id));
  }

  @Operation(summary = "Creates a new account", description = "A new account with the given number is created")
  @ApiResponse(responseCode = "201",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Account.class)
      ),
      description = "Returns the created account"
  )
  @ApiResponse(responseCode = "409",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Error.class)
      ),
      description = "An account with the given number already exists"
  )
  @ApiResponse(responseCode = "500",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Error.class)
      ),
      description = "An error occurred while creating accounts"
  )
  @Post(consumes = MediaType.APPLICATION_JSON)
  public Single<HttpResponse<Account>> create(@Valid @Body Single<AccountSaveCommand> command) {
    return command.map(c -> {
      var account = service.create(c);
      return HttpResponse
          .created(account)
          .headers(headers -> headers.location(location(account)));
    });
  }

  private URI location(Account account) {
    return URI.create(String.format("/api/accounts/%s", account.getId()));
  }

}
