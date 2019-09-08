package com.dayanfcosta.revolut.transfer.api;

import com.dayanfcosta.revolut.transfer.command.account.AccountSaveCommand;
import com.dayanfcosta.revolut.transfer.model.account.Account;
import com.dayanfcosta.revolut.transfer.service.AccountService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Single;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

/**
 * @author dayan.costa
 */
@Validated
@Controller("/api/accounts")
public class AccountResource {

  private final AccountService service;

  public AccountResource(AccountService service) {
    this.service = service;
  }

  @Get
  public Single<List<Account>> findAll() {
    return Single.just(service.findAll());
  }

  @Get("/{id}")
  public Single<Account> byId(@NotNull long id){
    return Single.just(service.findById(id));
  }

  @Post
  public Single<HttpResponse<Account>> save(@Valid @Body Single<AccountSaveCommand> command) {
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
